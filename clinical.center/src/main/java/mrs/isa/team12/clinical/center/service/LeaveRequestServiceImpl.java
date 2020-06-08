package mrs.isa.team12.clinical.center.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.LeaveRequest;
import mrs.isa.team12.clinical.center.model.MedicalPersonnel;
import mrs.isa.team12.clinical.center.model.enums.LeaveType;
import mrs.isa.team12.clinical.center.repository.LeaveRequestRepository;
import mrs.isa.team12.clinical.center.service.interfaces.LeaveRequestService;

@Service
@Transactional(readOnly = true)
public class LeaveRequestServiceImpl implements LeaveRequestService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private LeaveRequestRepository leaveReqRep;
	
	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public LeaveRequestServiceImpl(LeaveRequestRepository lrr) {
		this.leaveReqRep = lrr;
	}

	@Override
	public List<LeaveRequest> findAllByLeaveMedicalPersoneId(Long id) {
		logger.info("> findAllByMedicalPersoneId");
		List<LeaveRequest> lr = leaveReqRep.findAllByLeaveMedicalPersoneId(id);
		logger.info("< findAllByMedicalPersoneId");
		return lr;
	}

	@Override
	@Transactional(readOnly = false)
	public LeaveRequest save(LeaveRequest lr) {
		logger.info("> create");
		LeaveRequest leaveReq = leaveReqRep.save(lr);
		logger.info("< create");
		return leaveReq;
	}

	@Override
	public List<LeaveRequest> findAllByLeaveMedicalPersoneClinicId(Long id) {
		logger.info("> findAllByLeaveMedicalPersoneClinicId");
		List<LeaveRequest> lr = leaveReqRep.findAllByLeaveMedicalPersoneClinicId(id);
		logger.info("< findAllByLeaveMedicalPersoneClinicId");
		return lr;
	}

	@Override
	public LeaveRequest findOneById(Long id) {
		logger.info("> findOneById");
		LeaveRequest leaveReq = leaveReqRep.findOneById(id);
		logger.info("< findOneById");
		return leaveReq;
	}

	@Override
	public LeaveRequest update(Long id) {
		logger.info("> update id:{}", id);
		LeaveRequest leaveReq = findOneById(id);
		leaveReq.setApproved(true);
		LeaveRequest lr = save(leaveReq);
		//slanje mejla da je prihvacen leaveRequest
		sendAcceptedLeaveRequest(lr);
		logger.info("< update id:{}", id);
		return lr;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id, String description) {
		logger.info("> delete id:{}", id);
		LeaveRequest lr = findOneById(id);
		//slanje mejla da je odbijen leaveRequest
		sendDeclinedLeaveRequest(lr.getLeave().getMedicalPersone() ,description);
		lr.setActive(false);
		save(lr);
		logger.info("< delete id:{}", id);
	}

	@Override
	public void sendDeclinedLeaveRequest(MedicalPersonnel mp, String description) {
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		System.out.println("Slanje emaila...");
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(mp.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Leave Request declined!");
		mail.setText("Hello " + mp.getName() + ",\n\nYour leave request is declined!\n" +
				"With proper explanation:\"" + description + "\"" +
				"\nBest wishes,\nClinical center The Good Shepherd");
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}

	@Override
	public void sendAcceptedLeaveRequest(LeaveRequest lr) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy.");
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		System.out.println("Slanje emaila...");
		String leave = "";
		if(lr.getLeave().getType() == LeaveType.Paid) {
			leave = "paid leave";
		}else {
			leave = "vacation";
		}
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(lr.getLeave().getMedicalPersone().getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Leave request accepted!");
		mail.setText("Hello,"+ lr.getLeave().getMedicalPersone().getName() +" \n\n"  + "Your leave request is accepted!\n" + 
				"You are on " + leave + " from " + sdf1.format(lr.getLeave().getStartDate()) + " to " +  sdf1.format(lr.getLeave().getEndDate()) + ".\n" +
				"\nBest wishes,\nClinical center The Good Shepherd");
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}
	
	

}

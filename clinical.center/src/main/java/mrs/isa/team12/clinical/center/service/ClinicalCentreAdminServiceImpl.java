package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.dto.ClinicalCentreAdminPersonalInformationDto;
import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;
import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.repository.ClinicalCentreAdminRepository;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicalCenterAdminService;

@Service
@Transactional(readOnly = true)
public class ClinicalCentreAdminServiceImpl implements ClinicalCenterAdminService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ClinicalCentreAdminRepository clinicCentreAdminRep;
	
	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public ClinicalCentreAdminServiceImpl(ClinicalCentreAdminRepository clinicCentreAdminRep) {
		this.clinicCentreAdminRep = clinicCentreAdminRep;
	}
	
	@Override
	public ClinicalCentreAdmin findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		ClinicalCentreAdmin clinicalCentreAdmin = clinicCentreAdminRep.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return clinicalCentreAdmin;
	}
	
	@Override
	//cuvanje u bazu treba da bude pesimisticko je l ? posto on jos ne postoji, to da je pitamo jer ja ne znam kako to
	@Transactional(readOnly = false)
	public ClinicalCentreAdmin save(ClinicalCentreAdmin cca) {
		logger.info("> create");
		ClinicalCentreAdmin clinicCentreAdmin = clinicCentreAdminRep.save(cca);
		logger.info("< create");
		return clinicCentreAdmin;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ClinicalCentreAdmin updatePassword(Long id, String newPassword) {
		logger.info("> update id:{}", id);
		ClinicalCentreAdmin clinicalCentreAdminToUpdate = clinicCentreAdminRep.findOneById(id);
		clinicalCentreAdminToUpdate.setPassword(newPassword);
		clinicCentreAdminRep.save(clinicalCentreAdminToUpdate);
		logger.info("< update id:{}", id);
		return clinicalCentreAdminToUpdate;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ClinicalCentreAdmin update(ClinicalCentreAdminPersonalInformationDto editedProfile) {
		logger.info("> update id:{}", editedProfile.getId());
		ClinicalCentreAdmin clinicalCentreAdminToUpdate = clinicCentreAdminRep.findOneById(editedProfile.getId());
		clinicalCentreAdminToUpdate.setName(editedProfile.getName());
		clinicalCentreAdminToUpdate.setSurname(editedProfile.getSurname());
		clinicalCentreAdminToUpdate.setGender(editedProfile.getGender());
		clinicalCentreAdminToUpdate.setDateOfBirth(editedProfile.getDateOfBirth());
		clinicalCentreAdminToUpdate.setPhoneNumber(editedProfile.getPhoneNumber());
		clinicalCentreAdminToUpdate.setAddress(editedProfile.getAddress());
		clinicalCentreAdminToUpdate.setCity(editedProfile.getCity());
		clinicalCentreAdminToUpdate.setCountry(editedProfile.getCountry());
		clinicCentreAdminRep.save(clinicalCentreAdminToUpdate);
		logger.info("< update id:{}", editedProfile.getId());
		return clinicalCentreAdminToUpdate;
	}
	
	@Override
	public ClinicalCentreAdmin findOneByEmail(String email) {
		logger.info("> findOneByEmail email:{}", email);
		ClinicalCentreAdmin clinicalCentreAdmin = clinicCentreAdminRep.findOneByEmail(email);
		logger.info("< findOneByEmail email:{}", email);
		return clinicalCentreAdmin;
	}

	@Override
	public List<ClinicalCentreAdmin> findAll() {
		logger.info("> findAll");
		List<ClinicalCentreAdmin> clinicalCentreAdmins = clinicCentreAdminRep.findAll();
		logger.info("< findAll");
		return clinicalCentreAdmins;
	}

	@Async
	@Override
	public void sendNotificaitionAsync() {
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		
		for (ClinicalCentreAdmin cca : this.findAll()) {
			System.out.println("Slanje emaila...");
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(cca.getEmail());
			mail.setFrom(env.getProperty("spring.mail.username"));
			mail.setSubject("Registration request");
			mail.setText("Hello " + cca.getName() + ",\n\nYou have a new registration request.\n\n" + 
						"Best wishes,\nClinical center The Good Shepherd");
			javaMailSender.send(mail);
			System.out.println("Email poslat!");
		}
	}

	@Override
	@Async
	public void sendNotificaitionAsync(ClinicalCentreAdmin admin, RegisteredUser user, String description, boolean acceptance) {
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		System.out.println("Slanje emaila...");
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		if(acceptance == true) {
			mail.setSubject("Registration request accepted!");
			mail.setText("Hello " + user.getName() + ",\n\nAdmin " + admin.getEmail() + " accepted your registration request!\n" + 
					"You can login now! Wellcome! " +
					"\nBest wishes,\nClinical center The Good Shepherd");
		}else {
			mail.setSubject("Registration request rejected!");
			mail.setText("Hello " + user.getName() + ",\n\nAdmin " + admin.getEmail() + " declined your registration request!\n" +
					"With proper explanation:\"" + description + "\"" +
					"\nBest wishes,\nClinical center The Good Shepherd");
		}
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}
}
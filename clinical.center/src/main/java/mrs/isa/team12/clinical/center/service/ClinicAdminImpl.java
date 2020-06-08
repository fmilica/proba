package mrs.isa.team12.clinical.center.service;

import java.text.SimpleDateFormat;
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

import mrs.isa.team12.clinical.center.dto.ClinicAdminPersonalInformationDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.repository.ClinicAdminRepository;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicAdminService;

@Service
@Transactional(readOnly = true)
public class ClinicAdminImpl implements ClinicAdminService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ClinicAdminRepository clinicAdminRep;
	
	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public ClinicAdminImpl(ClinicAdminRepository clinicAdminRep) {
		this.clinicAdminRep = clinicAdminRep;
	}
	
	@Override
	public ClinicAdmin findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		ClinicAdmin clinicAdmin = clinicAdminRep.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return clinicAdmin;
	}

	//cuvanje u bazu treba da bude pesimisticko je l ? posto on jos ne postoji, to da je pitamo jer ja ne znam kako to
	@Transactional(readOnly = false)
	@Override
	public ClinicAdmin save(ClinicAdmin ca) {
		logger.info("> create");
		ClinicAdmin clinicAdmin = clinicAdminRep.save(ca);
		logger.info("< create");
		return clinicAdmin;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ClinicAdmin updatePassword(Long id, String newPassword) {
		logger.info("> update id:{}", id);
		ClinicAdmin clinicAdminToUpdate = clinicAdminRep.findOneById(id);
		clinicAdminToUpdate.setPassword(newPassword);
		clinicAdminRep.save(clinicAdminToUpdate);
		logger.info("< update id:{}", id);
		return clinicAdminToUpdate;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ClinicAdmin update(ClinicAdminPersonalInformationDto editedProfile) {
		logger.info("> update id:{}", editedProfile.getId());
		ClinicAdmin clinicAdminToUpdate = clinicAdminRep.findOneById(editedProfile.getId());
		clinicAdminToUpdate.setName(editedProfile.getName());
		clinicAdminToUpdate.setSurname(editedProfile.getSurname());
		clinicAdminToUpdate.setGender(editedProfile.getGender());
		clinicAdminToUpdate.setDateOfBirth(editedProfile.getDateOfBirth());
		clinicAdminToUpdate.setPhoneNumber(editedProfile.getPhoneNumber());
		clinicAdminToUpdate.setAddress(editedProfile.getAddress());
		clinicAdminToUpdate.setCity(editedProfile.getCity());
		clinicAdminToUpdate.setCountry(editedProfile.getCountry());
		clinicAdminRep.save(clinicAdminToUpdate);
		logger.info("< update id:{}", editedProfile.getId());
		return clinicAdminToUpdate;
	}
	
	@Override
	public ClinicAdmin findOneByEmail(String email) {
		logger.info("> findOneByEmail email:{}", email);
		ClinicAdmin clinicAdmin =  clinicAdminRep.findOneByEmail(email);
		logger.info("> findOneByEmail email:{}", email);
		return clinicAdmin;
	}
	
	@Override
	public List<ClinicAdmin> findAll() {
		logger.info("> findAll");
		List<ClinicAdmin> clinicAdmins = clinicAdminRep.findAll();
		logger.info("< findAll");
		return clinicAdmins;
	}
	
	@Override
	public List<ClinicAdmin> findAllByClinicId(Long clinicId) {
		logger.info("> findAllByClinicId id:{}", clinicId);
		List<ClinicAdmin> clinicAdmins = clinicAdminRep.findAllByClinicId(clinicId);
		logger.info("< findAllByClinicId id:{}", clinicId);
		return clinicAdmins;
	}
	
	//ne znam kako mejl i transakcije ??
	@Override
	@Async
	public void sendNotificaitionAsync(ClinicAdmin admin, Patient patient, Appointment appointment, boolean acceptance, boolean operation, boolean predefined) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy.");
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		String op = "Appointment";
		if(operation == true) {
			op = "Operation";
		}
		/*String adminFrom = "";
		if(predefined == true) {
			adminFrom = "thegoodshepherdadm@gmail.com";
			javaMailSender.setUsername(adminFrom);
			javaMailSender.setPassword("admin1tgs");
		}else {
			adminFrom = admin.getEmail();
			javaMailSender.setUsername(admin.getEmail());
			javaMailSender.setPassword(admin.getPassword());
		}*/
		System.out.println("Slanje emaila...");
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(patient.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		String disc = "";
		if(acceptance == true) {
			if(appointment.getDiscount() == null) {
				disc = "0";
			}
			else {
				disc = appointment.getDiscount()*100 + "";
			}
			mail.setSubject(op+" request accepted!");
			String mailText = "Hello " + patient.getName();
			
			String details = appointment.getAppType().getName() +" "+ op.toLowerCase() + " scheduled for " + 
					sdf1.format(appointment.getDate()) + " at " + appointment.getStartTime() + ":00" +
					" in clinic " + appointment.getClinic().getName() + ", ordination " + appointment.getOrdination().getName() +
					" " + appointment.getOrdination().getOrdinationNumber() +
					", by doctor "+ appointment.getDoctor().getName() + " " + appointment.getDoctor().getSurname() + ".\n" +
					"That "+ op.toLowerCase() +" costs " + appointment.getAppType().getPrice() + "\u20ac with " + disc + "% of discount.";
			
			if(predefined == true) {
				mailText += ",\n\nYou scheduled an appointment!\n";
				mailText += details;
				mailText += "You may see it in 'My Appointments' tab on your profile.";
			}
			else {
				mailText += ",\n\nAdmin " + admin.getEmail() + " accepted your "+ op.toLowerCase() +" request!\n";
				mailText += details;
				if(operation == true) {
					mailText += "You may see it in 'My Appointments' tab on your profile.";
				}else {
					mailText += "You may confirm it or decline it in 'My Appointments' tab on your profile.";
				}
			}
			mailText += "\nBest wishes,\nClinical center The Good Shepherd";
			mail.setText(mailText);
		}else {
			mail.setSubject("Appointment request rejected!");
			mail.setText("Hello " + patient.getName() + ",\n\nAdmin " + admin.getEmail() + " declined your appointment request!\n" + 
					"Best wishes,\nClinical center The Good Shepherd");
		}
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}

	@Override
	public void sendAppOperRequestNotification(Long clinicId, Doctor doctor, String type) {
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		System.out.println("Slanje emaila...");
		List<ClinicAdmin> cas = this.findAllByClinicId(clinicId);
		String[] toAdmins = new String[cas.size()];
		int i = 0;
		for (ClinicAdmin ca : cas) {
			toAdmins[i] = ca.getEmail();
			i++;
		}
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(toAdmins);
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Scheduling an "+type.toLowerCase());
		mail.setText("Hello " + ",\n\nDoctor " + doctor.getEmail() + " sent a request for an "+ type.toLowerCase() +".\n" + 
					"Best wishes,\nClinical center The Good Shepherd");
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}

	@Override
	public void sendLeaveRequestNotification(Long clinicId) {
		List<ClinicAdmin> admins = findAllByClinicId(clinicId);
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		System.out.println("Slanje emaila...");
		SimpleMailMessage mail = new SimpleMailMessage();
		String[] to = new String[admins.size()];
		int i = 0;
		for (ClinicAdmin ca : admins) {
			to[i] = ca.getEmail();
			i++;
		}
		mail.setTo(to);
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("New leave request!");
		mail.setText("Hello Admin,\n\n"  + "You have new leave requests waiting to be accepted or declined!\n" + 
				"\nBest wishes,\nClinical center The Good Shepherd");
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}
}

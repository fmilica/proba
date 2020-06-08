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

import mrs.isa.team12.clinical.center.dto.PatientProfileDto;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegistrationRequest;
import mrs.isa.team12.clinical.center.repository.PatientRepository;
import mrs.isa.team12.clinical.center.service.interfaces.PatientService;

@Service
@Transactional(readOnly = true)
public class PatientImpl implements PatientService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private PatientRepository patientRep;
	
	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public PatientImpl(PatientRepository patientRep) {
		this.patientRep = patientRep;
	}

	@Override
	@Transactional(readOnly = false)
	public Patient save(Patient p) {
		logger.info("> create patient");
		Patient pp = patientRep.save(p);
		logger.info("< create patient");
		return pp;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Patient deleteByRequestId(Long id) {
		Patient p = this.findOneByRegistrationRequestId(id);
		logger.info("> delete id{}:", p.getId());
		p.setActive(false);
		patientRep.save(p);
		logger.info("< delete id{}:", p.getId());
		return p;
	}
	
	@Override
	public Patient update(Patient p, RegistrationRequest regReq) {
		/*logger.info("> update id:{}",p.getId());
		Patient pp = this.findOneById(p.getId());
		pp.setRegistrationRequest(regReq);
		patientRep.save(pp);
		logger.info("< update id{}", p.getId());
		return pp;*/
		p.setRegistrationRequest(regReq);
		return patientRep.save(p);
	}
	
	@Override
	public Patient update(PatientProfileDto p) {
		Patient patientToUpdate = patientRep.findOneById(p.getId());
		patientToUpdate.setName(p.getName());
		patientToUpdate.setSurname(p.getSurname());
		patientToUpdate.setGender(p.getGender());
		patientToUpdate.setDateOfBirth(p.getDateOfBirth());
		patientToUpdate.setPhoneNumber(p.getPhoneNumber());
		patientToUpdate.setAddress(p.getAddress());
		patientToUpdate.setCity(p.getCity());
		patientToUpdate.setCountry(p.getCountry());
		//da li treba da se snimi mozda moze i bez snimanja tj sam da snimi ?
		return patientRep.save(patientToUpdate);
	}

	@Override
	public Patient updatePassword(Long id, String newPassword) {
		Patient patientToUpdate = patientRep.findOneById(id);
		patientToUpdate.setPassword(newPassword);
		
		return patientRep.save(patientToUpdate);
	}

	@Override
	public Patient findOneByRegistrationRequestId(Long id) {
		logger.info("> findOneByRegistrationRequestId id:{}", id);
		Patient pp = patientRep.findOneByRegistrationRequestId(id);
		logger.info("< findOneByRegistrationRequestId id:{}", id);
		return pp;
	}
	
	@Override
	public Patient findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		Patient patient = patientRep.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return patient;
	}

	@Override
	public Patient findOneByEmail(String email) {
		return patientRep.findOneByEmail(email);
	}

	@Override
	public List<Patient> findAll() {
		return patientRep.findAll();
	}

	@Override
	public List<Patient> filter(String name, String surname, String secNum) {
		return patientRep.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndSecurityNumberContainingIgnoreCase(name, surname, secNum);
	}

	@Override
	public Patient findOneBySecurityNumber(String securityNumber) {
		return patientRep.findOneBySecurityNumber(securityNumber);
	}

	@Override
	@Async
	public void sendNotificaitionAsync(ClinicAdmin admin, Patient patient) {
		javaMailSender.setUsername(env.getProperty("spring.mail.username"));
		javaMailSender.setPassword(env.getProperty("spring.mail.password"));
		System.out.println("Slanje emaila...");
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(admin.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Scheduling an appointment");
		mail.setText("Hello " + admin.getName() + ",\n\nPatient " + patient.getEmail() + " sent a request for an appointment.\n" + 
					"Best wishes,\nClinical center The Good Shepherd");
		javaMailSender.send(mail);
		System.out.println("Email poslat!");
	}
}

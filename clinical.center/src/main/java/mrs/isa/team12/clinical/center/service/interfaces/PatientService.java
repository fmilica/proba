package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import mrs.isa.team12.clinical.center.dto.PatientProfileDto;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegistrationRequest;

public interface PatientService {

	Patient findOneById(Long id);
	
	Patient findOneByEmail(String email);
	
	Patient save(Patient p);
	
	Patient updatePassword(Long id, String newPassword) throws Exception;
	
	Patient update(PatientProfileDto p) throws Exception;
	
	Patient update(Patient p, RegistrationRequest r) throws Exception;
	
	@Async
	public void sendNotificaitionAsync(ClinicAdmin admin, Patient patient);
	
	public List<Patient> findAll();
	
	public List<Patient> filter(String name, String surname, String secNum);
	
	Patient findOneBySecurityNumber(String securityNumber);
	
	Patient findOneByRegistrationRequestId(Long id);
	
	Patient deleteByRequestId(Long id);
}

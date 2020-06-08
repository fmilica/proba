package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import mrs.isa.team12.clinical.center.dto.ClinicAdminPersonalInformationDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Patient;

public interface ClinicAdminService {
	
	ClinicAdmin findOneById(Long id);
	
	ClinicAdmin save(ClinicAdmin ca);
	
	ClinicAdmin updatePassword(Long id, String newPassword);
	
	ClinicAdmin update(ClinicAdminPersonalInformationDto editedProfile);
	
	ClinicAdmin findOneByEmail(String email);
	
	List<ClinicAdmin> findAll();
	
	List<ClinicAdmin> findAllByClinicId(Long clinicId);
	
	@Async
	public void sendNotificaitionAsync(ClinicAdmin admin, Patient patient,Appointment appointment, boolean acceptance, boolean operation, boolean predefined);
	
	@Async
	public void sendAppOperRequestNotification(Long clinicId, Doctor doctor, String type);
	
	@Async
	public void sendLeaveRequestNotification(Long clinicId);
}

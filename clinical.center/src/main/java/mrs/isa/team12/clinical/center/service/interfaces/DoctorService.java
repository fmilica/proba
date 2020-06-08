package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;
import java.util.Set;

import mrs.isa.team12.clinical.center.dto.DoctorPersonalInformationDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Patient;

public interface DoctorService {
	
	Doctor findOneById(Long id);
	
	Doctor save(Doctor d);
	
	void delete(Doctor d);
	
	Doctor updatePassword(Long doctorId, String newPassword);
	
	Doctor update(DoctorPersonalInformationDto editedDoctor, Set<AppointmentType> appTypes);
	
	Doctor findOneByEmail(String email);
	
	List<Doctor> findAll();
	
	List<Doctor> findAllByClinicId(Long id);
	
	List<Doctor> findAllByAppointmentTypes(AppointmentType a);
	
	List<Doctor> findAllByAppointmentTypesIn(List<AppointmentType> types);
	
	List<Doctor> findAllByClinicIdAndAppointmentTypes(Long clinicId, AppointmentType type);

	List<Doctor> findAllByClinicAndAppointmentTypesIn(Clinic clinic, List<AppointmentType> types);
	
	void sendNotificaitionAsync(ClinicAdmin ca, Patient p, Appointment a, boolean acceptance, Set<Doctor> doctors);
	
	void sendDoctorNotificaitionAsync(ClinicAdmin ca, Patient p, Appointment a, boolean acceptance, Doctor d);
}

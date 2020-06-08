package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.Clinic;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {
	
	//AppointmentRequest findOneByName(String name);
	
	AppointmentRequest findOneById(Long id);
	
	List<AppointmentRequest> findAllByClinic(Clinic clinic);
	
	List<AppointmentRequest> findAllByClinicAndApproved(Clinic clinic, Boolean approved);
	
	void deleteOneById(Long id);
	
	List<AppointmentRequest> findAllByAppointmentDoctorId(Long id);
	
}

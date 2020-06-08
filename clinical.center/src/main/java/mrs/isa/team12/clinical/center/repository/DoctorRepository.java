package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.Doctor;

//Interface for Doctor database access
public interface DoctorRepository extends JpaRepository<Doctor, Long>{
	
	Doctor findOneByEmail(String email);
	
	Doctor findOneById(Long id);
	
	List<Doctor> findAll();
	
	List<Doctor> findAllByClinicId(Long id);
	/*
	@Query("SELECT d "
			+ "FROM Doctor d, AppointmentTypeDoctor ad, AppointmentType a "
			+ "WHERE d.id = ad.doctorId AND ad.appointmentTypeId = a.id AND a.name = ?1")*/
	List<Doctor> findAllByAppointmentTypes(AppointmentType a);
	
	List<Doctor> findAllByAppointmentTypesIn(List<AppointmentType> types);
	
	List<Doctor> findAllByClinicAndAppointmentTypesIn(Clinic clinic, List<AppointmentType> types);
	
	List<Doctor> findAllByClinicIdAndAppointmentTypes(Long clinicId, AppointmentType type);
}

package mrs.isa.team12.clinical.center.repository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	
	Appointment findOneById(Long id);
	
	List<Appointment> findAllByPatientIdAndDoctorId(Long patientId, Long doctorId);
	
	List<Appointment> findAllByClinicIdAndConfirmedAndFinished(Long id, Boolean confirmed, Boolean finished);
	
	List<Appointment> findAllByPatientIdAndFinished(Long patientId, Boolean finished);
	
	List<Appointment> findAllByPatientIdAndConfirmed(Long patientId, Boolean confirmed);
	
	List<Appointment> findAllByPatientIdAndConfirmedAndFinished(Long patientId, Boolean confirmed, Boolean finished);
	
	List<Appointment> findAllByClinicIdAndPatient(Long clinicId, Patient patient);
	
	List<Appointment> findAllByDoctorId(Long id);
	
	List<Appointment> findAllByPatientId(Long id);
	
	List<Appointment> findAllByClinicIdAndFinished(Long id, Boolean finished);
	
	List<Appointment> findAllByDoctorsIn(Set<Doctor> doctors);
	
	List<Appointment> findAllByTypeAndDoctorsIn(OrdinationType ot, Set<Doctor> doctors);
	
	List<Appointment> findAllByDoctorIdAndDateBetween(Long id, Date d1, Date d2);
}

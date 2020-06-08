package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import mrs.isa.team12.clinical.center.model.MedicalReport;
import mrs.isa.team12.clinical.center.model.Nurse;

public interface MedicalReportService {
	
	MedicalReport save(MedicalReport mr);
	
	MedicalReport findOneById(Long id);
	
	void update(Long id, Nurse nurse) throws Exception;
	
	List<MedicalReport> findAll();
	
	List<MedicalReport> findAllByNurseIdAndAppointmentPatientId(Long nurse_id, Long patient_id);
}

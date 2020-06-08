package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;
import java.util.Set;

import mrs.isa.team12.clinical.center.model.Diagnosis;
import mrs.isa.team12.clinical.center.model.Prescription;

public interface PrescriptionService {
	
	Prescription save(Prescription p);
	
	Prescription update(String medicine, Prescription p, Set<Diagnosis> d) throws Exception;
	
	List<Prescription> findAll();
	
	List<Prescription> findAllByDiagnosisId(Long id);
	
	Prescription findOneById(Long id);
	
	Prescription findOneByMedicine(String medicine);
}

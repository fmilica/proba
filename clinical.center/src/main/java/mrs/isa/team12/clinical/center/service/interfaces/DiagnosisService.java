package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import mrs.isa.team12.clinical.center.model.Diagnosis;

public interface DiagnosisService {
	
	Diagnosis findOneById(Long id);
	
	Diagnosis save(Diagnosis d);
	
	Diagnosis findOneByName(String name);
	
	List<Diagnosis> findAll();
}

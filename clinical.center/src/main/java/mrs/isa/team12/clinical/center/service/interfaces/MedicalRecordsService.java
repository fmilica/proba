package mrs.isa.team12.clinical.center.service.interfaces;

import mrs.isa.team12.clinical.center.model.MedicalRecords;

public interface MedicalRecordsService {
	
	MedicalRecords findOneById(Long id);
	
	MedicalRecords save(MedicalRecords mr);
}

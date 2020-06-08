package mrs.isa.team12.clinical.center.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.MedicalRecords;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecords, Long>{
	
	MedicalRecords save(MedicalRecords mr);
	
	MedicalRecords findOneById(Long id);
}

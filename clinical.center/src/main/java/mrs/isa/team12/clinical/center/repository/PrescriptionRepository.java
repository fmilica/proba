package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long>{
	
	List<Prescription> findAll();
	
	List<Prescription> findAllByDiagnosisId(Long id);
	
	Prescription findOneById(Long id);
	
	Prescription findOneByMedicine(String medicine);
}

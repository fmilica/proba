package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.DiagnosePerscription;

public interface DiagnosisPrescriptionRepository extends JpaRepository<DiagnosePerscription, Long>{
	
	List<DiagnosePerscription> findAll();
	
	DiagnosePerscription findOneById(Long id);
}

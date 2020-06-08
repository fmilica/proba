package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Diagnosis;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
	
	List<Diagnosis> findAll();
	
	Diagnosis findOneByName(String name);
	
	Diagnosis save(Diagnosis d);
	
	Diagnosis findOneById(Long id);
}

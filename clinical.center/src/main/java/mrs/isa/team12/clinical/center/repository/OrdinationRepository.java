package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;

public interface OrdinationRepository extends JpaRepository<Ordination, Long>{

	Ordination findOneByName(String name);
	
	Ordination findOneByNameAndOrdinationNumber(String name, Integer ordinationNumber);
	
	Ordination findOneByClinicIdAndNameAndOrdinationNumber(Long clinicId, String name, Integer ordinationNumber);
	
	Ordination findOneById(Long id);
	
	List<Ordination> findAllByClinicId(Long clinicId);
	
	List<Ordination> findAllByClinicIdAndType(Long clinicId, OrdinationType type);
	
	List<Ordination> findAllByClinicIdAndNameContainingIgnoreCaseAndType(Long clinicId, String name, OrdinationType type);
	
	List<Ordination> findAllByClinicIdAndOrdinationNumberAndType(Long clinicId, Integer ordinationNumber, OrdinationType type);
	
	List<Ordination> findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType(Long clinicId, String name, Integer ordinationNumber, OrdinationType type);
}
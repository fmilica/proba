package mrs.isa.team12.clinical.center.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.ClinicalCentre;

public interface ClinicalCentreRepository extends JpaRepository<ClinicalCentre, Long>  {
	
	public ClinicalCentre findOneByName(String name);
	
}
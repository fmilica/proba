package mrs.isa.team12.clinical.center.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;

public interface ClinicalCentreAdminRepository extends JpaRepository<ClinicalCentreAdmin, Long> {

	ClinicalCentreAdmin findOneById(Long id);
	ClinicalCentreAdmin findOneByEmail(String email);
	
}
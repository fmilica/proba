package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.ClinicAdmin;

//Interface for Clinic Admin database access
public interface ClinicAdminRepository extends JpaRepository<ClinicAdmin, Long> {
	
	ClinicAdmin findOneById(Long id);
	
	ClinicAdmin findOneByEmail(String email);
	
	List<ClinicAdmin> findAllByClinicId(Long clinicId);
}

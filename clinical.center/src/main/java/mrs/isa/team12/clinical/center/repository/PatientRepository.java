package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	
	Patient findOneById(Long id);
	
	Patient findOneByEmail(String email);
	
	List<Patient> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCaseAndSecurityNumberContainingIgnoreCase(String name, String surname, String securityNumber);

	Patient findOneBySecurityNumber(String securityNumber);
	
	Patient findOneByRegistrationRequestId(Long id);
	
	void deleteById(Long id);
}

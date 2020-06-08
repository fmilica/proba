package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.RegistrationRequest;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
	
	RegistrationRequest findOneById(Long id);
	
	RegistrationRequest save(RegistrationRequest rr);
	
	List<RegistrationRequest> findAll();
	
	void deleteById(Long id);
}

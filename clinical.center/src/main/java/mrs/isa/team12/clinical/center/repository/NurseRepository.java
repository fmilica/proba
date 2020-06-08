package mrs.isa.team12.clinical.center.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Nurse;

//Interface for Nurse database access
public interface NurseRepository extends JpaRepository<Nurse, Long>{
	
	Nurse findOneById(Long id);

	Nurse findOneByEmail(String email);
	
}

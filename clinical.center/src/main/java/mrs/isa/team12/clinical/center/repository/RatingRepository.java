package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	Rating save(Rating r);

	List<Rating> findAll();
	
	List<Rating> findAllByClinicId(Long id);
	
	List<Rating> findAllByDoctorId(Long id);
}

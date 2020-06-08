package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import mrs.isa.team12.clinical.center.model.Rating;

public interface RatingService {

	Rating save(Rating r);

	List<Rating> findAll();
	
	List<Rating> findAllByClinicId(Long id);
	
	List<Rating> findAllByDoctorId(Long id);
	
}

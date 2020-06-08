package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.Rating;
import mrs.isa.team12.clinical.center.repository.RatingRepository;
import mrs.isa.team12.clinical.center.service.interfaces.RatingService;

@Service
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private RatingRepository ratingRepository;
	
	@Autowired
	public RatingServiceImpl(RatingRepository rr) {
		this.ratingRepository = rr;
	}
	
	@Override
	@Transactional(readOnly = false)
	public Rating save(Rating r) {
		logger.info("> create");
		Rating rr = ratingRepository.save(r);
		logger.info("< create");
		return rr;
	}

	@Override
	public List<Rating> findAll() {
		logger.info("> findAll");
		List<Rating> ratings = ratingRepository.findAll();
		logger.info("< findAll");
		return ratings;
	}

	@Override
	public List<Rating> findAllByClinicId(Long id) {
		logger.info("> findAllByClinicId");
		List<Rating> ratings = ratingRepository.findAllByClinicId(id);
		logger.info("< findAllByClinicId");
		return ratings;
	}

	@Override
	public List<Rating> findAllByDoctorId(Long id) {
		logger.info("> findAllByDoctorId");
		List<Rating> ratings = ratingRepository.findAllByDoctorId(id);
		logger.info("< findAllByDoctorId");
		return ratings;
	}
}

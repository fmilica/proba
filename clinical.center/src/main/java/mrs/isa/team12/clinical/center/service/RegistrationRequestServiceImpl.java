package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegistrationRequest;
import mrs.isa.team12.clinical.center.repository.RegistrationRequestRepository;
import mrs.isa.team12.clinical.center.service.interfaces.RegistrationRequestService;

@Service
@Transactional(readOnly = true)
public class RegistrationRequestServiceImpl implements RegistrationRequestService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private RegistrationRequestRepository requestRepository;
	
	@Autowired
	public RegistrationRequestServiceImpl(RegistrationRequestRepository requestRepository) {
		this.requestRepository = requestRepository;
	}

	@Override
	@Transactional(readOnly = false)
	public RegistrationRequest save(RegistrationRequest rr) {
		logger.info("> create");
		RegistrationRequest r = requestRepository.save(rr);
		logger.info("< create");
		return r;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RegistrationRequest update(Long reqId, boolean approved) {
		logger.info("> update id:{}", reqId);
		
		RegistrationRequest rr = this.findOneById(reqId);
		
		rr.setApproved(approved);
		
		requestRepository.save(rr);
		logger.info("< update id:{}", rr.getId());
		return rr;
	}
	/*
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(Long id) {
		logger.info("> delete id:{}", id);
		requestRepository.deleteById(id);
		logger.info("< delete id:{}", id);
	}
	*/
	@Override
	public List<RegistrationRequest> findAll() {
		logger.info("> findAll");
		List<RegistrationRequest> requests = requestRepository.findAll();
		logger.info("< findAll");
		return requests;
	}
	
	@Override
	public RegistrationRequest findOneById(Long id) {
		logger.info("> findOneById");
		RegistrationRequest rr = requestRepository.findOneById(id);
		logger.info("< findOneById");
		return rr;
	}
}

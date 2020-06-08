package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.Diagnosis;
import mrs.isa.team12.clinical.center.repository.DiagnosisRepository;
import mrs.isa.team12.clinical.center.service.interfaces.DiagnosisService;

@Service
@Transactional(readOnly = true)
public class DiagnosisServiceImpl implements DiagnosisService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private DiagnosisRepository diagnosisRepository;
	
	@Autowired
	public DiagnosisServiceImpl(DiagnosisRepository dr) {
		this.diagnosisRepository = dr;
	}

	@Override
	public Diagnosis findOneById(Long id) {
		logger.info("> findOneById");
		Diagnosis diagnosis = diagnosisRepository.findOneById(id);
		logger.info("< findOneById");
		return diagnosis;
	}
	
	//:(
	@Transactional(readOnly = false)
	@Override
	public Diagnosis save(Diagnosis d) {
		logger.info("> create");
		Diagnosis diagnosis = diagnosisRepository.save(d);
		logger.info("< create");
		return diagnosis;
	}

	@Override
	public Diagnosis findOneByName(String name) {
		logger.info("> findOneByName name{}", name);
		Diagnosis diagnosis =  diagnosisRepository.findOneByName(name);
		logger.info("< findOneByName name{}", name);
		return diagnosis;
	}
	
	@Override
	public List<Diagnosis> findAll() {
		logger.info("> findAll");
		List<Diagnosis> diagnosises = diagnosisRepository.findAll();
		logger.info("> findAll");
		return diagnosises;
	}
}

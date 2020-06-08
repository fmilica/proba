package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.ClinicalCentre;
import mrs.isa.team12.clinical.center.repository.ClinicalCentreRepository;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicalCenterService;

@Service
@Transactional(readOnly = true)
public class ClinicalCentreServiceImpl implements ClinicalCenterService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ClinicalCentreRepository clinicalCentreRepository;
	
	@Autowired
	public ClinicalCentreServiceImpl(ClinicalCentreRepository ccr) {
		this.clinicalCentreRepository = ccr;
	}
	
	//save ne znam kako :(
	@Override
	@Transactional(readOnly = false)
	public ClinicalCentre save(ClinicalCentre cc) {
		logger.info("> create");
		ClinicalCentre clinicalCentre = clinicalCentreRepository.save(cc);
		logger.info("< create");
		return clinicalCentre;
	}
	
	@Override
	public ClinicalCentre findOneByName(String name) {
		logger.info("> findOneByName name:{}", name);
		ClinicalCentre clinicalCentre = clinicalCentreRepository.findOneByName(name);
		logger.info("< findOneByName name:{}", name);
		return clinicalCentre;
	}
	
	@Override
	public List<ClinicalCentre> findAll() {
		logger.info("> findAll");
		List<ClinicalCentre> clinicalCentres = clinicalCentreRepository.findAll();
		logger.info("< findAll");
		return clinicalCentres;
	}
}

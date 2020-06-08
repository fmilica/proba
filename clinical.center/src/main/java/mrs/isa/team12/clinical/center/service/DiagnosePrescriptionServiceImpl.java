package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.DiagnosePerscription;
import mrs.isa.team12.clinical.center.repository.DiagnosisPrescriptionRepository;
import mrs.isa.team12.clinical.center.service.interfaces.DiagnosisPrescriptionService;

@Service
@Transactional(readOnly = true)
public class DiagnosePrescriptionServiceImpl implements DiagnosisPrescriptionService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private DiagnosisPrescriptionRepository diagnosePrescriptionRep;
	
	@Autowired
	public DiagnosePrescriptionServiceImpl(DiagnosisPrescriptionRepository dpr) {
		this.diagnosePrescriptionRep = dpr;
	}

	@Override
	public DiagnosePerscription findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		DiagnosePerscription diagnosePerscription = diagnosePrescriptionRep.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return diagnosePerscription;
	}
	
	//:(
	@Override
	@Transactional(readOnly = false)
	public DiagnosePerscription save(DiagnosePerscription ds) {
		logger.info("> create");
		DiagnosePerscription diagnosePerscription = diagnosePrescriptionRep.save(ds);
		logger.info("< create");
		return diagnosePerscription;
	}
	
	@Override
	public List<DiagnosePerscription> findAll() {
		logger.info("> findAll");
		List<DiagnosePerscription> diagnosePerscription = diagnosePrescriptionRep.findAll();
		logger.info("< findAll");
		return diagnosePerscription;
	}
}

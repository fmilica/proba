package mrs.isa.team12.clinical.center.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.Diagnosis;
import mrs.isa.team12.clinical.center.model.Prescription;
import mrs.isa.team12.clinical.center.repository.PrescriptionRepository;
import mrs.isa.team12.clinical.center.service.interfaces.PrescriptionService;

@Service
@Transactional(readOnly = true)
public class PrescriptionServiceImpl implements PrescriptionService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private PrescriptionRepository prescriptionRepository;
	
	@Autowired
	public PrescriptionServiceImpl(PrescriptionRepository pr) {
		this.prescriptionRepository= pr;
	}

	@Override
	@Transactional(readOnly = false)
	public Prescription save(Prescription p) {
		logger.info("> create");
		Prescription pp = prescriptionRepository.save(p);
		logger.info("< create");
		return pp;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Prescription update(String medicine, Prescription p, Set<Diagnosis> d) {
		Prescription pp = this.findOneByMedicine(medicine);
		if(pp == null) {
			logger.info("> create");
			p.setDiagnosis(d);
			pp = prescriptionRepository.save(p);
			logger.info("< create");
			return pp;
		}
		return null;
	}

	@Override
	public List<Prescription> findAll() {
		logger.info("> findAll");
		List<Prescription> presc = prescriptionRepository.findAll();
		logger.info("< findAll");
		return presc;
	}

	@Override
	public List<Prescription> findAllByDiagnosisId(Long id) {
		logger.info("< findAllByDiagnosisId");
		List<Prescription> presc = prescriptionRepository.findAllByDiagnosisId(id);
		logger.info("< findAllByDiagnosisId");
		return presc;
	}

	@Override
	public Prescription findOneById(Long id) {
		logger.info("< findOneById");
		Prescription presc = prescriptionRepository.findOneById(id);
		logger.info("< findOneById");
		return presc;
	}

	@Override
	public Prescription findOneByMedicine(String medicine) {
		logger.info("< findOneByMedicine");
		Prescription presc = prescriptionRepository.findOneByMedicine(medicine);
		logger.info("< findOneByMedicine");
		return presc;
	}
}

package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.MedicalReport;
import mrs.isa.team12.clinical.center.model.Nurse;
import mrs.isa.team12.clinical.center.repository.MedicalReportRepository;
import mrs.isa.team12.clinical.center.service.interfaces.MedicalReportService;

@Service
@Transactional(readOnly = true)
public class MedicalReportServiceImpl implements MedicalReportService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private MedicalReportRepository medicalreportRepository;
	
	@Autowired
	public MedicalReportServiceImpl(MedicalReportRepository mrr) {
		this.medicalreportRepository = mrr;
	}

	@Transactional(readOnly = false)
	@Override
	public MedicalReport save(MedicalReport mr) {
		logger.info("> create");
		MedicalReport medicalReport =  medicalreportRepository.save(mr);
		logger.info("< create");
		return medicalReport;
	}

	@Override
	public List<MedicalReport> findAll() {
		logger.info("> findAll");
		List<MedicalReport> medicalReport =  medicalreportRepository.findAll();
		logger.info("< findAll");
		return medicalReport;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void update(Long id, Nurse nurse) throws Exception {
		logger.info("> update id:{}", id);
		MedicalReport mr = this.findOneById(id);
		mr.setVerified(true);
		mr.setNurse(nurse);
		medicalreportRepository.save(mr);
		logger.info("< update id:{}",id);
	}

	@Override
	public MedicalReport findOneById(Long id) {
		logger.info("> findOneById");
		MedicalReport mr = medicalreportRepository.findOneById(id);
		logger.info("< findOneById");
		return mr;
	}

	@Override
	public List<MedicalReport> findAllByNurseIdAndAppointmentPatientId(Long nurse_id, Long patient_id) {
		logger.info("> findAllByNurseId");
		List<MedicalReport> mrs = medicalreportRepository.findAllByNurseIdAndAppointmentPatientId(nurse_id, patient_id);
		logger.info("< findAllByNurseId");
		return mrs;
	}
}

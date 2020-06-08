package mrs.isa.team12.clinical.center.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.MedicalRecords;
import mrs.isa.team12.clinical.center.repository.MedicalRecordRepository;
import mrs.isa.team12.clinical.center.service.interfaces.MedicalRecordsService;

@Service
@Transactional(readOnly = true)
public class MedicalRecordsServiceImpl implements MedicalRecordsService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private MedicalRecordRepository medicalRecordsRepository;
	
	@Autowired
	public MedicalRecordsServiceImpl(MedicalRecordRepository mrr) {
		this.medicalRecordsRepository = mrr;
	}

	@Override
	public MedicalRecords findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		MedicalRecords medicalRecords =  medicalRecordsRepository.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return medicalRecords;
	}

	//:(
	@Transactional(readOnly = false)
	@Override
	public MedicalRecords save(MedicalRecords mr) {
		logger.info("> create");
		MedicalRecords medicalRecords = medicalRecordsRepository.save(mr);
		logger.info("< create");
		return medicalRecords;
	}
}

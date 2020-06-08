package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.Report;
import mrs.isa.team12.clinical.center.repository.ReportRepository;
import mrs.isa.team12.clinical.center.service.interfaces.ReportService;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ReportRepository reportRepository;
	
	@Autowired
	public ReportServiceImpl(ReportRepository rr) {
		this.reportRepository = rr;
	}
	
	@Override
	public List<Report> findAll() {
		logger.info("> findAll");
		List<Report> reports = reportRepository.findAll();
		logger.info("< findAll");
		return reports;
	}

	@Override
	public List<Report> findAllByClinicId(Long clinicId) {
		logger.info("> findAllByClinicId");
		List<Report> reports = reportRepository.findAllByClinicId(clinicId);
		logger.info("< findAllByClinicId");
		return reports;
	}

	@Override
	@Transactional(readOnly = false)
	public Report save(Report report) {
		logger.info("> create report");
		Report r = reportRepository.save(report);
		logger.info("< create report");
		return r;
	}

}

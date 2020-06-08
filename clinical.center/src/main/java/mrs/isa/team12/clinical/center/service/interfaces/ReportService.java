package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import mrs.isa.team12.clinical.center.model.Report;

public interface ReportService {
	
	List<Report> findAll();
	
	List<Report> findAllByClinicId(Long clinicId);
	
	Report save(Report report);
}

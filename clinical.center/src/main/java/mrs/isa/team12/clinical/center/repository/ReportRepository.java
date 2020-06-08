package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{

	List<Report> findAll();
	
	List<Report> findAllByClinicId(Long clinicId);
	
	Report save(Report report);
}

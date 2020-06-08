package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>{
	
	List<LeaveRequest> findAllByLeaveMedicalPersoneId(Long id);
	
	List<LeaveRequest> findAllByLeaveMedicalPersoneClinicId(Long id);
	
	LeaveRequest findOneById(Long id);
}

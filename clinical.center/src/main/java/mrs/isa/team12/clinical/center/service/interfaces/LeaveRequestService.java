package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import mrs.isa.team12.clinical.center.model.LeaveRequest;
import mrs.isa.team12.clinical.center.model.MedicalPersonnel;

public interface LeaveRequestService {
	
	List<LeaveRequest> findAllByLeaveMedicalPersoneId(Long id);
	
	LeaveRequest save(LeaveRequest lr);
	
	List<LeaveRequest> findAllByLeaveMedicalPersoneClinicId(Long id);
	
	LeaveRequest findOneById(Long id);
	
	LeaveRequest update(Long id) throws Exception;
	
	void delete(Long id, String description);
	
	@Async
	public void sendDeclinedLeaveRequest(MedicalPersonnel mp ,String description);
	
	@Async
	public void sendAcceptedLeaveRequest(LeaveRequest lr);
}

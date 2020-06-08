package mrs.isa.team12.clinical.center.service.interfaces;
import java.util.List;

import mrs.isa.team12.clinical.center.model.RegistrationRequest;

public interface RegistrationRequestService {
	
	RegistrationRequest save(RegistrationRequest rr);
	
	RegistrationRequest update(Long reqId, boolean approved) throws Exception;
	/*
	void deleteById(Long id);
	*/
	List<RegistrationRequest> findAll();
	
	RegistrationRequest findOneById(Long id);
}

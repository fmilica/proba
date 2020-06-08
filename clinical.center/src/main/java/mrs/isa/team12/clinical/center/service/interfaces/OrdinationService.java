package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import mrs.isa.team12.clinical.center.dto.OrdinationDto;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;

public interface OrdinationService {
	
	Ordination save(Ordination o);
	
	void delete(Ordination o);
	
	Ordination update(Ordination o, OrdinationDto edited);
	
	void update(Long id, AppointmentRequest ar) throws Exception;
	
	Ordination update(Long clinicId, String name, Integer ordNumber, Ordination ord, Clinic clinic);

	Ordination findOneByName(String name);
	
	Ordination findOneByNameAndOrdinationNumber(String name, Integer ordinationNumber);
	
	Ordination findOneByClinicIdAndNameAndOrdinationNumber(Long clinicId, String name, Integer ordinationNumber);
	
	Ordination findOneById(Long id);
	
	List<Ordination> findAllByClinicId(Long clinicId);
	
	List<Ordination> findAllByClinicIdAndType(Long clinicId, OrdinationType type);
	
	List<Ordination> findAllByClinicIdAndNameContainingIgnoreCaseAndType(Long clinicId, String name, OrdinationType type);
	
	List<Ordination> findAllByClinicIdAndOrdinationNumberAndType(Long clinicId, Integer ordinationNumber, OrdinationType type);
	
	List<Ordination> findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType(Long clinicId, String name, Integer ordinationNumber, OrdinationType type);
	
}

package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import mrs.isa.team12.clinical.center.model.DiagnosePerscription;

public interface DiagnosisPrescriptionService {

	DiagnosePerscription findOneById(Long id);
	
	DiagnosePerscription save(DiagnosePerscription ds);
	
	List<DiagnosePerscription> findAll();
}

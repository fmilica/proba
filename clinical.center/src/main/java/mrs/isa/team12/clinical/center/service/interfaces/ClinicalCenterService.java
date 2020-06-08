package mrs.isa.team12.clinical.center.service.interfaces;


import java.util.List;

import mrs.isa.team12.clinical.center.model.ClinicalCentre;

public interface ClinicalCenterService {

	public ClinicalCentre save(ClinicalCentre cc);
	
	public ClinicalCentre findOneByName(String name);
	
	public List<ClinicalCentre> findAll();
}

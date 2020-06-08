
package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import mrs.isa.team12.clinical.center.dto.ClinicalCentreAdminPersonalInformationDto;
import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;
import mrs.isa.team12.clinical.center.model.RegisteredUser;

public interface ClinicalCenterAdminService {
	
	ClinicalCentreAdmin findOneById(Long id);
	
	ClinicalCentreAdmin save(ClinicalCentreAdmin cca);

	ClinicalCentreAdmin updatePassword(Long id, String newPassword);
	
	ClinicalCentreAdmin update(ClinicalCentreAdminPersonalInformationDto editedProfile);
	
	ClinicalCentreAdmin findOneByEmail(String email);
	
	List<ClinicalCentreAdmin> findAll();

	@Async
	public void sendNotificaitionAsync();
	
	@Async
	public void sendNotificaitionAsync(ClinicalCentreAdmin admin, RegisteredUser user, String description, boolean acceptance);
}

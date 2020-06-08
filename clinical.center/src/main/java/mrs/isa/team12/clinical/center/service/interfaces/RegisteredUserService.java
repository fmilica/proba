package mrs.isa.team12.clinical.center.service.interfaces;

import mrs.isa.team12.clinical.center.model.RegisteredUser;

public interface RegisteredUserService {

	RegisteredUser findOneByEmail(String email);
	
	RegisteredUser findOneBySecurityNumber(String number);
}

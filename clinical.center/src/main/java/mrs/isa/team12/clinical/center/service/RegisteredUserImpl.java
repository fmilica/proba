package mrs.isa.team12.clinical.center.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.repository.RegisteredUserRepository;
import mrs.isa.team12.clinical.center.service.interfaces.RegisteredUserService;

@Service
@Transactional(readOnly = true)
public class RegisteredUserImpl implements RegisteredUserService {

	//private AppointmentRepository appointmentRepository;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private RegisteredUserRepository rep;
	
	@Autowired
	public RegisteredUserImpl(RegisteredUserRepository rep) {
		super();
		this.rep = rep;
	}

	@Override
	public RegisteredUser findOneByEmail(String email) {
		logger.info("> findOneByEmail");
		RegisteredUser ru = rep.findOneByEmail(email);
		logger.info("< findOneByEmail");
		return ru;
		
	}

	@Override
	public RegisteredUser findOneBySecurityNumber(String number) {
		logger.info("> findOneBySecurityNumber");
		RegisteredUser ru = rep.findOneBySecurityNumber(number);
		logger.info("< findOneBySecurityNumber");
		return ru;
	}

}

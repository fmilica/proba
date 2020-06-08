package mrs.isa.team12.clinical.center.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.dto.AppointmentTypeDto;
import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.repository.AppointmentTypeRepository;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentTypeService;

@Service
@Transactional(readOnly = true)
public class AppointmentTypeImpl implements AppointmentTypeService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private AppointmentTypeRepository appointmentTypeRep;
		
	@Autowired
	public AppointmentTypeImpl(AppointmentTypeRepository appointmentTypeRep) {
		this.appointmentTypeRep = appointmentTypeRep;
	}
	
	//cuvanje u bazu treba da bude pesimisticko je l ? posto on jos ne postoji, to da je pitamo jer ja ne znam kako to
	@Transactional(readOnly = false)
	@Override
	public AppointmentType save(AppointmentType at) {
		logger.info("> create");
		AppointmentType appType = appointmentTypeRep.save(at);
		logger.info("< create");
		return appType;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(AppointmentType at) {
		logger.info("> delete id:{}", at.getId());
		at.setActive(false);
		appointmentTypeRep.save(at);
		logger.info("< delete id:{}", at.getId());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public AppointmentType update(AppointmentType at, AppointmentTypeDto edited) {
		logger.info("> update id:{}", at.getId());
		at.setName(edited.getName());
		at.setDuration(edited.getDuration());
		at.setPrice(edited.getPrice());
		AppointmentType updated = appointmentTypeRep.save(at);
		logger.info("< update id:{}", at.getId());
		return updated;
	}

	@Override
	public AppointmentType findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		AppointmentType appType = appointmentTypeRep.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return appType;
	}
	
	@Override
	public AppointmentType findOneByName(String name) {
		logger.info("> findOneByName name:{}", name);
		AppointmentType appType = appointmentTypeRep.findOneByName(name);
		logger.info("< findOneByName name:{}", name);
		return appType;
	}
	
	@Override
	public AppointmentType findOneByNameAndClinicId(String name, Long id) {
		logger.info("> findOneByNameAndClinicId name:{},{}", name, id);
		AppointmentType appType = appointmentTypeRep.findOneByNameAndClinicId(name, id);
		logger.info("< findOneByNameAndClinicId name:{},{}", name, id);
		return appType;
	}


	@Override
	public List<AppointmentType> findAll() {
		logger.info("> findAll");
		List<AppointmentType> appTypes = appointmentTypeRep.findAll();
		logger.info("< findAll");
		return appTypes;
	}
	
	
	@Override
	public List<AppointmentType> findAllByName(String name) {
		logger.info("> findAllByName");
		List<AppointmentType> appTypes = appointmentTypeRep.findAllByName(name);
		logger.info("< findAllByName");
		return appTypes;
	}

	
	@Override
	public List<AppointmentType> findAllByClinicId(Long clinicId) {
		logger.info("> findAllByClinicId");
		List<AppointmentType> appTypes = appointmentTypeRep.findAllByClinicId(clinicId);
		logger.info("< findAllByClinicId");
		return appTypes;
	}
	
	@Override
	public Set<AppointmentType> findAllByClinicIdAndNameIn(Long clinicId, List<String> appTypeNames) {
		logger.info("> findAllByClinicIdAndNameIn");
		Set<AppointmentType> appTypes = appointmentTypeRep.findAllByClinicIdAndNameIn(clinicId, appTypeNames);
		logger.info("< findAllByClinicIdAndNameIn");
		return appTypes;
	}
	
}

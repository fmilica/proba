package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.dto.ClinicDto;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.repository.ClinicRepository;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicService;

@Service
@Transactional(readOnly = true)
public class ClinicImpl implements ClinicService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ClinicRepository repository;
	
	@Autowired
	public ClinicImpl(ClinicRepository rep) {
		this.repository = rep;
	}
	
	@Override
	public Clinic findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		Clinic clinic = repository.findOneById(id);
		logger.info("< findOneById id:{}", id);
		return clinic;
	}
	
	//ne znam :(
	@Transactional(readOnly = false)
	@Override
	public Clinic save(Clinic c) {
		logger.info("> create");
		Clinic clinic = repository.save(c);
		logger.info("< create");
		return clinic;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Clinic update(ClinicDto editedClinic) {
		logger.info("> update id{}", editedClinic.getId());
		Clinic clinicToUpdate = repository.findOneById(editedClinic.getId());
		clinicToUpdate.setName(editedClinic.getName());
		clinicToUpdate.setAddress(editedClinic.getAddress());
		clinicToUpdate.setCity(editedClinic.getCity());
		clinicToUpdate.setCountry(editedClinic.getCountry());
		clinicToUpdate.setDescription(editedClinic.getDescription());
		Clinic updated = repository.save(clinicToUpdate);
		logger.info("< update id{}", updated.getId());
		return updated;
	}

	@Override
	public Clinic findOneByName(String name) {
		logger.info("> findOneByName name:{}", name);
		Clinic clinic = repository.findOneByName(name);
		logger.info("< findOneByName name:{}", name);
		return clinic;
	}
	
	@Override
	public List<Clinic> findAll() {
		logger.info("> findAll");
		List<Clinic> clinics = repository.findAll();
		logger.info("< findAll");
		return clinics;
	}
	
	@Override
	public List<Clinic> findAllByAppointmentTypeId(Long appTypeId) {
		logger.info("> findAllByAppointmentTypeId appTypeId:{}", appTypeId);
		List<Clinic> clinics = repository.findAllByAppointmentTypeId(appTypeId);
		logger.info("< findAllByAppointmentTypeId appTypeId:{}", appTypeId);
		return clinics;
	}
	
	@Override
	public List<Clinic> findAllByAppointmentTypeName(String appTypeName) {
		logger.info("> findAllByAppointmentTypeName appTypeName:{}", appTypeName);
		List<Clinic> clinics = repository.findAllByAppointmentTypeName(appTypeName);
		logger.info("< findAllByAppointmentTypeName appTypeName:{}", appTypeName);
		return clinics;
	}
}

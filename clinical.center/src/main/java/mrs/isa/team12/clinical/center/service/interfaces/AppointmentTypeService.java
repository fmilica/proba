package mrs.isa.team12.clinical.center.service.interfaces;

import java.util.List;
import java.util.Set;

import mrs.isa.team12.clinical.center.dto.AppointmentTypeDto;
import mrs.isa.team12.clinical.center.model.AppointmentType;

public interface AppointmentTypeService {
	
	AppointmentType save(AppointmentType at);
	
	void delete(AppointmentType at);
	
	AppointmentType update(AppointmentType at, AppointmentTypeDto edited);
	
	AppointmentType findOneById(Long id);
	
	AppointmentType findOneByName(String name);
	
	AppointmentType findOneByNameAndClinicId(String name, Long id);
	
	List<AppointmentType> findAll();
	
	List<AppointmentType> findAllByName(String name);
	
	List<AppointmentType> findAllByClinicId(Long clinicId);
	
	Set<AppointmentType> findAllByClinicIdAndNameIn(Long clinicId, List<String> appTypeNames);
}

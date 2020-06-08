package mrs.isa.team12.clinical.center.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.isa.team12.clinical.center.model.AppointmentType;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {

	AppointmentType findOneById(Long id);
	
	AppointmentType findOneByName(String name);
	
	List<AppointmentType> findAllByName(String name);
	
	List<AppointmentType> findAllByClinicId(Long clinicId);
	
	AppointmentType findOneByNameAndClinicId(String name, Long id);
	
	Set<AppointmentType> findAllByClinicIdAndNameIn(Long clinicId, List<String> appTypeNames);
}

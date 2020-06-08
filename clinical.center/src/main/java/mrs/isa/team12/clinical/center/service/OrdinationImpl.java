package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.dto.OrdinationDto;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;
import mrs.isa.team12.clinical.center.repository.OrdinationRepository;
import mrs.isa.team12.clinical.center.service.interfaces.OrdinationService;

@Service
@Transactional(readOnly = true)
public class OrdinationImpl implements OrdinationService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private OrdinationRepository ordinationRep;
	
	@Autowired
	public OrdinationImpl(OrdinationRepository ordinationRep) {
		this.ordinationRep = ordinationRep;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Ordination o) {
		logger.info("> delete id:{}", o.getId());
		o.setActive(false);
		ordinationRep.save(o);
		logger.info("< delete id:{}", o.getId());
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Ordination update(Ordination o, OrdinationDto edited) {
		logger.info("> update id:{}", o.getId());
		o.setName(edited.getName());
		o.setOrdinationNumber(edited.getOrdinationNumber());
		o.setType(edited.getType());
		Ordination updated = ordinationRep.save(o);
		logger.info("< update id:{}", o.getId());
		return updated;
	}
	
	@Override
	public Ordination findOneByName(String name) {
		logger.info("> findOneByName");
		Ordination o = ordinationRep.findOneByName(name);
		logger.info("< findOneByName");
		return o;
	}
	
	@Override
	public Ordination findOneByNameAndOrdinationNumber(String name, Integer ordinationNumber) {
		logger.info("> findOneByNameAndOrdinationNumber");
		Ordination o = ordinationRep.findOneByNameAndOrdinationNumber(name, ordinationNumber);
		logger.info("< findOneByNameAndOrdinationNumber");
		return o;
	}
	
	
	@Override
	public Ordination findOneByClinicIdAndNameAndOrdinationNumber(Long clinicId, String name, Integer ordinationNumber) {
		logger.info("> findOneByClinicIdAndNameAndOrdinationNumber");
		Ordination o = ordinationRep.findOneByClinicIdAndNameAndOrdinationNumber(clinicId, name, ordinationNumber);
		logger.info("< findOneByClinicIdAndNameAndOrdinationNumber");
		return o;
	}

	@Override
	public List<Ordination> findAllByClinicId(Long clinicId) {
		logger.info("> findAllByClinicId");
		List<Ordination> o = ordinationRep.findAllByClinicId(clinicId);
		logger.info("< findAllByClinicId");
		return o;
	}
	
	@Override
	public List<Ordination> findAllByClinicIdAndType(Long clinicId, OrdinationType type) {
		logger.info("> findAllByClinicIdAndType");
		List<Ordination> o = ordinationRep.findAllByClinicIdAndType(clinicId, type);
		logger.info("< findAllByClinicIdAndType");
		return o;
	}
	
	@Override
	public List<Ordination> findAllByClinicIdAndNameContainingIgnoreCaseAndType(Long clinicId, String name,
			OrdinationType type) {
		logger.info("> findAllByClinicIdAndNameContainingIgnoreCaseAndType");
		List<Ordination> o = ordinationRep.findAllByClinicIdAndNameContainingIgnoreCaseAndType(clinicId, name, type);
		logger.info("< findAllByClinicIdAndNameContainingIgnoreCaseAndType");
		return o;
	}

	@Override
	public List<Ordination> findAllByClinicIdAndOrdinationNumberAndType(Long clinicId, Integer ordinationNumber,
			OrdinationType type) {
		logger.info("> findAllByClinicIdAndOrdinationNumberAndType");
		List<Ordination> o = ordinationRep.findAllByClinicIdAndOrdinationNumberAndType(clinicId, ordinationNumber, type);
		logger.info("< findAllByClinicIdAndOrdinationNumberAndType");
		return o;
	}

	@Override
	public List<Ordination> findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType(Long clinicId, String name,
			Integer ordinationNumber, OrdinationType type) {
		logger.info("> findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType");
		List<Ordination> o = ordinationRep.findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType(clinicId, name, ordinationNumber, type);
		logger.info("< findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType");
		return o;
	}

	@Override
	@Transactional(readOnly = false)
	public Ordination save(Ordination o) {
		logger.info("> create");
		Ordination ord = ordinationRep.save(o);
		logger.info("< create");
		return ord;
	}

	@Override
	public Ordination findOneById(Long id) {
		logger.info("> findOneById");
		Ordination o = ordinationRep.findOneById(id);
		logger.info("< findOneById");
		return o;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void update(Long id, AppointmentRequest ar) {
		logger.info("> update id:{}",id);
		Ordination o = this.findOneById(id);
		o.addAppointment(ar.getAppointment());
		ar.getAppointment().setOrdination(o);
		ordinationRep.save(o);
		logger.info("< update id:{}",id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Ordination update(Long clinicId, String name, Integer ordNumber, Ordination ord, Clinic clinic) {
		Ordination o = this.findOneByClinicIdAndNameAndOrdinationNumber(clinicId, name, ordNumber);
		if(o == null) {
			logger.info("> create");
			ord.setClinic(clinic);
			this.save(ord);
			logger.info("< create");
			return ord;
		}
		return null;
	}

}

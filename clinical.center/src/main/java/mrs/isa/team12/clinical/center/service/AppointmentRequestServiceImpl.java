package mrs.isa.team12.clinical.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.repository.AppointmentRequestRepository;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentRequestService;

@Service
@Transactional(readOnly = true)
public class AppointmentRequestServiceImpl implements AppointmentRequestService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AppointmentRequestRepository appointmentRequestRep;
	
	@Autowired
	public AppointmentRequestServiceImpl(AppointmentRequestRepository atr) {
		this.appointmentRequestRep = atr;
	}
	
	@Override
	public AppointmentRequest findOneById(Long id) {
		logger.info("> findOneById id:{}", id);
		AppointmentRequest appRequest = appointmentRequestRep.findOneById(id);
		logger.info("< findOneById id: {}", id);
		return appRequest;
	}

	//cuvanje u bazu treba da bude pesimisticko je l ? posto on jos ne postoji, to da je pitamo jer ja ne znam kako to
	@Override
	@Transactional(readOnly = false)
	public AppointmentRequest save(AppointmentRequest ar) {
		logger.info("> create");
		AppointmentRequest savedAppReq = appointmentRequestRep.save(ar);
		logger.info("< create");
		return savedAppReq;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(AppointmentRequest ar) {
		logger.info("> delete id:{}", ar.getId());
		ar.setActive(false);
		appointmentRequestRep.save(ar);
		logger.info("< delete id:{}", ar.getId());
	}
	
	@Override
	public List<AppointmentRequest> findAllByClinic(Clinic clinic) {
		logger.info("> findAllByClinic");
		List<AppointmentRequest> appRequests = appointmentRequestRep.findAllByClinic(clinic);
		logger.info("> findAllByClinic");
		return appRequests;
	}

	@Override
	public List<AppointmentRequest> findAllByClinicAndApproved(Clinic clinic, Boolean approved) {
		logger.info("> findAllByClinicAndApproved");
		List<AppointmentRequest> appRequests = appointmentRequestRep.findAllByClinicAndApproved(clinic, approved);
		logger.info("< findAllByClinicApproved");
		return appRequests;
	}

	@Override
	public List<AppointmentRequest> findAllByAppointmentDoctorId(Long id) {
		logger.info("> findAllByAppointmentDoctorId");
		List<AppointmentRequest> ars = appointmentRequestRep.findAllByAppointmentDoctorId(id);
		logger.info("< findAllByAppointmentDoctorId");
		return ars;
	}

}

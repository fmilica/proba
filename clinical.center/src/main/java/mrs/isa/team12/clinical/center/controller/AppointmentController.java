package mrs.isa.team12.clinical.center.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.AppointmentDto;
import mrs.isa.team12.clinical.center.dto.AppointmentFollowupDto;
import mrs.isa.team12.clinical.center.dto.AppointmentPredefinedDto;
import mrs.isa.team12.clinical.center.dto.CalendarDto;
import mrs.isa.team12.clinical.center.dto.DoctorsAppointmentDto;
import mrs.isa.team12.clinical.center.dto.LeaveDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.LeaveRequest;
import mrs.isa.team12.clinical.center.model.Nurse;
import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentRequestService;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentService;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentTypeService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicAdminService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicService;
import mrs.isa.team12.clinical.center.service.interfaces.DoctorService;
import mrs.isa.team12.clinical.center.service.interfaces.LeaveRequestService;
import mrs.isa.team12.clinical.center.service.interfaces.OrdinationService;
import mrs.isa.team12.clinical.center.service.interfaces.PatientService;

@RestController
@RequestMapping("theGoodShepherd/appointment")
public class AppointmentController {
	
	private AppointmentService appointmentService;
	private AppointmentTypeService appointmentTypeService;
	private AppointmentRequestService appointmentRequestService;
	private PatientService patientService;
	private DoctorService doctorService;
	private OrdinationService ordinationService;
	private ClinicService clinicService;
	private ClinicAdminService adminService;
	private LeaveRequestService leaveReqService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public AppointmentController(AppointmentService appointmentService, PatientService patientService,
			AppointmentTypeService appointmentTypeService, DoctorService doctorService,
			OrdinationService ordinationService, ClinicService clinicService,
			AppointmentRequestService appointmentRequestService, ClinicAdminService adminService, 
			LeaveRequestService leaveReqService) {
		this.appointmentService = appointmentService;
		this.patientService = patientService;
		this.appointmentTypeService = appointmentTypeService;
		this.doctorService = doctorService;
		this.ordinationService = ordinationService;
		this.clinicService = clinicService;
		this.appointmentRequestService = appointmentRequestService;
		this.adminService = adminService;
		this.leaveReqService = leaveReqService;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment
	 HTTP request for getting all appointments in one clinic
	 returns ResponseEntity object
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllClinicAppointments() {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical administrators can view clinic appointments.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Appointment> appointments = appointmentService.findAllByClinicIdAndConfirmedAndFinished(currentUser.getClinic().getId(), true, false);
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			dto.add(new AppointmentDto(a));
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/freePredefined
	 HTTP request for getting all clinics free predefined appointments
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/freePredefined", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllClinicFreePredefinedAppointments() {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical administrators can view clinic appointments.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Appointment> appointments = appointmentService.findAllByClinicIdAndPatient(currentUser.getClinic().getId(), null);
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			dto.add(new AppointmentDto(a));
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/upcoming
	 HTTP request for getting all upcoming clinic appointments
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/upcoming", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllClinicUpcomingAppointments() {
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical administrators can view clinic appointments.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Appointment> appointments = appointmentService.findAllByClinicIdAndFinished(currentUser.getClinic().getId(), false);
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			if(a.getDoctor() != null && a.getPatient() != null & a.getOrdination() != null) {
				dto.add(new AppointmentDto(a));
			}
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
		
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/delete/{appointmentId}
	 HTTP request for deleting appointment
	 parameter: String
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/delete/{appointmentId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteAppointment(@PathVariable Long appointmentId) {
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admins can remove appointments from their clinic.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Appointment appointment = appointmentService.findById(appointmentId);
		
		appointmentService.delete(appointment);
	}

	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/allPatientFinished
	 HTTP request for getting all confirmed appointments for the logged in patient
	 returns ResponseEntity object
	 */
	@GetMapping(value="allPatientFinished",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllPatientFinishedApps() {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can view all his appointments!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Appointment> appointments = appointmentService.findAllByPatientIdAndFinished(currentUser.getId(), true);
		
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			dto.add(new AppointmentDto(a));
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/allPatientConfirmed
	 HTTP request for getting all confirmed appointments for the logged in patient
	 returns ResponseEntity object
	 */
	@GetMapping(value="allPatientConfirmed",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllPatientConfirmedApps() {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can view all his appointments!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		//mislim da je ovo greska da ovde treba da bude confirmed true and finished false posto su upcoming
		//List<Appointment> appointments = appointmentService.findAllByPatientIdAndConfirmed(currentUser.getId(), true);
		List<Appointment> appointments = appointmentService.findAllByPatientIdAndConfirmedAndFinished(currentUser.getId(), true, false);
		
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			dto.add(new AppointmentDto(a));
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/allPatientUnconfirmed
	 HTTP request for getting all unconfirmed appointments for the logged in patient
	 returns ResponseEntity object
	 */
	@GetMapping(value="allPatientUnconfirmed",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllPatientUnconfirmedApps() {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can view all his appointments!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Appointment> appointments = appointmentService.findAllByPatientIdAndConfirmed(currentUser.getId(), false);
		
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			// ako nema ordinaciju, nije odobren jos uvek!
			// ako nije odobren, znaci da je predefinisan
			if (a.getOrdination() != null && a.getConfirmed() == false) {
				dto.add(new AppointmentDto(a));
			}
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/appointment/confirmApp/{appId}
	 HTTP request for appointment confirmation
	 returns ResponseEntity object
	 */
	@PostMapping(value="confirmApp/{appId}",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppointmentDto> confirmAppointment(@PathVariable("appId") Long appId) throws Exception {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can confirm his appointment!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Appointment app = null;
		
		try {
			app = appointmentService.update(appId);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified appointment does not exist!");
		}
		
		return new ResponseEntity<>(new AppointmentDto(app), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/appointment/declineApp/{appId}
	 HTTP request for declining an appointment
	 returns ResponseEntity object
	 */
	@PostMapping(value="declineApp/{appId}",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public void declineAppointment(@PathVariable("appId") Long appId) {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can confirm his appointment!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Appointment app = appointmentService.findById(appId);
		appointmentService.delete(app);

		// PACIJENT MOZE DA PRIHVATI/ODBIJE SAMO NE-PREDEFINISANE
		// PREDEFINISANI IDU DIREKTNO U NJIHOVE POTVRDJENE
		// ne znam kako da obrisem :(((
		// help pliz :(
		/*
		Appointment app = appointmentService.findById(appId);
		AppointmentRequest appReq = app.getAppointmentRequest();
		
		try {
			appointmentRequestService.delete(appReq);
			appointmentService.delete(app);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified appointment does not exist!");
		}
		*/
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointment/available/{clinicId}
	 HTTP request for getting all appointments in one clinic
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/available/{clinicId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentDto>> getAllAvailableClinicAppointments(@PathVariable("clinicId") Long clinicId) {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can view available clinic appointments.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Appointment> appointments = appointmentService.findAllByClinicIdAndPatient(clinicId, null);
		List<AppointmentDto> dto = new ArrayList<AppointmentDto>();
		for(Appointment a : appointments) {
			dto.add(new AppointmentDto(a));
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/appointment/createPatientApp
	 HTTP request for creating a patient appointment and appointment request
	 receives Long appId
	 returns ResponseEntity object
	 */
	@PostMapping(value = "createPatientApp",
				 consumes = MediaType.APPLICATION_JSON_VALUE,
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppointmentPredefinedDto> createPatientApp(@RequestBody AppointmentPredefinedDto appDto) {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can schedule a new appointment.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Patient patient = patientService.findOneById(currentUser.getId());
		// kreiranje novog appointmenta - pacijent
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = new Date(dt.parse(appDto.getDate()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		AppointmentType type = appointmentTypeService.findOneByNameAndClinicId(appDto.getAppTypeName(), appDto.getClinicId());
		Clinic c = clinicService.findOneById(appDto.getClinicId());
		Doctor d = doctorService.findOneById(appDto.getDoctorId());
																		//confirmed, finished
		Appointment appointment = new Appointment(date, appDto.getTime(), type, false, false, d, c, patient);
		
		// klinici dodamo novi pregled
		c.addAppointment(appointment);
		// doktoru dodamo novi pregled
		d.addAppointment(appointment);
		// pacijentu dodamo novi pregled
		patient.addAppointment(appointment);
		appointment = appointmentService.save(appointment);
		
		Date today = new Date(new java.util.Date().getTime());
		// kreiramo zahtev za pregled
																			// approved
		AppointmentRequest appRequest = new AppointmentRequest(appointment, today, false, c);
		appointmentRequestService.save(appRequest);
		
		// dodavanje u appointment njegov appointment request
		appointment.setAppointmentRequest(appRequest);
		appointmentService.save(appointment);

		return new ResponseEntity<>(appDto, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/appointment/createPredefined
	 HTTP request for creating a predefined appointment
	 receives Long appId
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/createPredefined",
				 consumes = MediaType.APPLICATION_JSON_VALUE,
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppointmentDto> createPredefinedAppointment(@RequestBody AppointmentPredefinedDto appDto) {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a clinic administrator can create a predefined appointment!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		// kreiranje novog predefinisanog appointmenta
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = new Date(dt.parse(appDto.getDate()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		AppointmentType type = appointmentTypeService.findOneByNameAndClinicId(appDto.getAppTypeName(), currentUser.getClinic().getId());
		Clinic c = clinicService.findOneById(currentUser.getClinic().getId());
		Doctor d = doctorService.findOneById(appDto.getDoctorId());
		Ordination o = ordinationService.findOneById(appDto.getOrdinationId());
		
		Appointment appointment = new Appointment(date, appDto.getTime(), type, appDto.getDiscount(), true, false, o, d, c);
		
		// klinici dodamo novi pregled
		c.addAppointment(appointment);
		// doktoru dodamo novi pregled
		d.addAppointment(appointment);
		// ordinaciji dodamo novi pregled
		o.addAppointment(appointment);
		
		appointment = appointmentService.save(appointment);
		
		return new ResponseEntity<>(new AppointmentDto(appointment), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/appointment/schedule/{appId}
	 HTTP request for scheduling a predefined appointment
	 receives Long appId
	 returns ResponseEntity object
	 */
	@PostMapping(value = "schedule/{appId}",
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppointmentDto> schedulePredefinedApp(@PathVariable("appId") Long appId) throws Exception {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can schedule an appointments.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		//Patient patient = patientService.findOneById(currentUser.getId());
		Appointment app;
		try {
			app = appointmentService.update(currentUser, appId); //ovde promenjeno sa patient na currentUser jer vec ga imamo, sto bi ga dobavljali
			app.getAppType();
			adminService.sendNotificaitionAsync(null, currentUser, app, true, false, true);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified appointment does not exist!");
		}
		return new ResponseEntity<>(new AppointmentDto(app), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/appointment/createFollowupRequest
	 HTTP request for creating a followup examination or operation request
	 receives AppointmentFollowupDto
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/createFollowupRequest",
				 consumes = MediaType.APPLICATION_JSON_VALUE,
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AppointmentFollowupDto> createPredefinedAppointment(@RequestBody AppointmentFollowupDto appDto) {
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a doctor can schedule a followup exam!");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		// kreiranje novog followup appointmenta
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = new Date(dt.parse(appDto.getDate()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		AppointmentType type = appointmentTypeService.findOneById(appDto.getAppTypeId());
		Doctor doctor = doctorService.findOneById(currentUser.getId());
		Clinic clinic = doctor.getClinic();
		Patient patient = patientService.findOneBySecurityNumber(appDto.getPatientSecurityNumber());
		
		Appointment appointment = new Appointment(date, appDto.getTime(), type, false, false, doctor, clinic, patient);
		String appType = "";
		
		if (appDto.getOperation()) {
			appointment.setType(OrdinationType.OperatingRoom);
			appType = "Operation";
		} else {
			appointment.setType(OrdinationType.ConsultingRoom);
			appType = "Appointment";
		}
		
		// klinici dodamo novi pregled
		clinic.addAppointment(appointment);
		// doktoru dodamo novi pregled
		doctor.addAppointment(appointment);
		// pacijentu dodamo novi pregled
		patient.addAppointment(appointment);
		appointment = appointmentService.save(appointment);
		
		// kreiramo zahtev za pregled
		Date today = new Date(new java.util.Date().getTime());
																			// approved
		AppointmentRequest appRequest = new AppointmentRequest(appointment, today, false, clinic);
		appointmentRequestService.save(appRequest);
		
		// dodavanje u appointment njegov appointment request
		appointment.setAppointmentRequest(appRequest);
		appointmentService.save(appointment);
		
		// SLANJE MEJLA SVIM ADMINIMA KLINIKE DA IMAJU NOVI ZAHTEV ZA FOLLOWUP, DA NE KAZEM KONTROLU!
		// SLANJE MEJLA SVIM ADMINIMA KLINIKE DA IMAJU NOVI ZAHTEV ZA OPERACIJU!
		// ZAVISNO OD TIPA NARAVNO
		// GORE VEC IMA IF ZA TIP, AKO MOZE DA SE IZKORISTI
		adminService.sendAppOperRequestNotification(currentUser.getClinic().getId(), doctor, appType);
		
		return new ResponseEntity<>(appDto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/appointment/medicalPersonnel
	 HTTP request for getting doctor and nurses calendar
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/medicalPersonnel",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CalendarDto> getDoctorAppointments() {
		
		Doctor doctor = null;
		Nurse nurse = null;
		Patient patient = null;
		RegisteredUser ru = null;
		try {
			doctor = (Doctor) session.getAttribute("currentUser");
		}catch(ClassCastException e) {
			try {
				nurse = (Nurse) session.getAttribute("currentUser");
			}catch(ClassCastException ex) {
				try {
					patient = (Patient) session.getAttribute("currentUser");
				}catch(ClassCastException exc) {
					ru = (RegisteredUser) session.getAttribute("currentUser");
					//throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctors,nurses and patients can view their callendar.");
				}
			}
		}
		
		/*if(doctor == null && nurse == null && patient == null && ru==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}*/
		
		List<DoctorsAppointmentDto> appDtos = new ArrayList<DoctorsAppointmentDto>();
		List<LeaveDto> reqDtos = new ArrayList<LeaveDto>();
		List<Appointment> apps;
		List<Appointment> oper;
		List<LeaveRequest> reqs;
		if(doctor != null) {
			apps = appointmentService.findAllByDoctorId(doctor.getId());
			Set<Doctor> doctors = new HashSet<Doctor>();
			doctors.add(doctor);
			oper = appointmentService.findAllByDoctorsIn(doctors);
			reqs = leaveReqService.findAllByLeaveMedicalPersoneId(doctor.getId());
			
			//doktorovi pregledi i operacije
			for (Appointment ar : apps) {
				if(ar.getAppointmentRequest() == null) {
					if(ar.getConfirmed()) {
						appDtos.add(new DoctorsAppointmentDto(ar));
					}
				}else {
					if(ar.getAppointmentRequest().getApproved() && ar.getConfirmed()) {
						appDtos.add(new DoctorsAppointmentDto(ar));
					}
				}
			}
			//operacije u kojima doktor ucestvuje
			for (Appointment ar : oper) {
				if(ar.getAppointmentRequest() == null) {
					if(ar.getConfirmed()) {
						appDtos.add(new DoctorsAppointmentDto(ar));
					}
				}else {
					if(ar.getAppointmentRequest().getApproved() && ar.getConfirmed()) {
						appDtos.add(new DoctorsAppointmentDto(ar));
					}
				}
			}
			//bolovanje i odmori doktora
			for (LeaveRequest leaveRequest : reqs) {
				if(leaveRequest.getApproved()) {
					reqDtos.add(new LeaveDto(leaveRequest.getLeave().getStartDate(), leaveRequest.getLeave().getEndDate(), leaveRequest.getLeave().getType()+""));
				}
			}
			
			return new ResponseEntity<>(new CalendarDto(reqDtos, appDtos, "doctor"), HttpStatus.CREATED);
		}
		else if (nurse != null) {
			reqs = leaveReqService.findAllByLeaveMedicalPersoneId(nurse.getId());
			
			for (LeaveRequest leaveRequest : reqs) {
				if(leaveRequest.getApproved()) {
					reqDtos.add(new LeaveDto(leaveRequest.getLeave().getStartDate(), leaveRequest.getLeave().getEndDate(), leaveRequest.getLeave().getType()+""));
				}
			}
			return new ResponseEntity<>(new CalendarDto(reqDtos, appDtos, "nurse"), HttpStatus.CREATED);
		}else if (patient != null){
			apps = appointmentService.findAllByPatientId(patient.getId());
			
			for (Appointment ar : apps) {
				if(ar.getAppointmentRequest() == null) {
					if(ar.getConfirmed()) {
						appDtos.add(new DoctorsAppointmentDto(ar));
					}
				}else {
					if(ar.getAppointmentRequest().getApproved() && ar.getConfirmed()) {
						appDtos.add(new DoctorsAppointmentDto(ar));
					}
				}
			}
			
			return new ResponseEntity<>(new CalendarDto(reqDtos, appDtos, "patient"), HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(new CalendarDto(), HttpStatus.CREATED);
		}
	}
	
	
}

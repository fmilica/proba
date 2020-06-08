package mrs.isa.team12.clinical.center.controller;

import java.util.ArrayList;
import java.util.List;

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

import mrs.isa.team12.clinical.center.dto.AppointmentReqDto;
import mrs.isa.team12.clinical.center.dto.ClinicAdminDto;
import mrs.isa.team12.clinical.center.dto.ClinicAdminPersonalInformationDto;
import mrs.isa.team12.clinical.center.dto.DoctorDto;
import mrs.isa.team12.clinical.center.dto.RegisteredUserDto;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentRequestService;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicAdminService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicService;
import mrs.isa.team12.clinical.center.service.interfaces.DoctorService;
import mrs.isa.team12.clinical.center.service.interfaces.OrdinationService;
import mrs.isa.team12.clinical.center.service.interfaces.RegisteredUserService;


@RestController
@RequestMapping("theGoodShepherd/clinicAdmin")
public class ClinicAdminController {
	
	private ClinicAdminService adminService;
	private ClinicService clinicService;
	private DoctorService doctorService;
	private AppointmentRequestService appointmentReqService;
	private AppointmentService appointmentService;
	private OrdinationService ordinationService;
	private RegisteredUserService userService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public ClinicAdminController(ClinicAdminService adminService, ClinicService clinicService,
			DoctorService doctorService, AppointmentRequestService appointmentReqService, AppointmentService appointmentService,
			OrdinationService ordinationService, RegisteredUserService userService) {
		this.adminService = adminService;
		this.clinicService = clinicService;
		this.doctorService = doctorService;
		this.appointmentReqService = appointmentReqService;
		this.appointmentService = appointmentService;
		this.ordinationService = ordinationService;
		this.userService = userService;
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicAdmin/changePassword/{password}
	 HTTP request for changing password
	 receives String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "changePassword/{password}")
	public ResponseEntity<RegisteredUserDto> changePassword(@PathVariable String password){
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admin can change his password.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		ClinicAdmin updated = adminService.updatePassword(currentUser.getId(), password);
		
		session.setAttribute("currentUser", updated);
		
		return new ResponseEntity<>(new RegisteredUserDto(updated), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/clinicAdmin/personalInformation
	 HTTP request for clinic admins personal information
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/personalInformation",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicAdminPersonalInformationDto> viewPersonalInformation() {
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admin can view his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		ClinicAdmin clinicAdmin = adminService.findOneById(currentUser.getId());

		return new ResponseEntity<>(new ClinicAdminPersonalInformationDto(clinicAdmin) , HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicAdmin/editPersonalInformation
	 HTTP request for editing doctors personal information
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/editPersonalInformation")
	public ResponseEntity<ClinicAdminPersonalInformationDto> editPersonalInformation(@RequestBody ClinicAdminPersonalInformationDto editedProfile) {

		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admin can edit his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		if (!currentUser.getEmail().equals(editedProfile.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email can't be changed!");
		}
		
		editedProfile.setId(currentUser.getId());
		
		ClinicAdmin clinicAdmin = adminService.update(editedProfile);
		
		// postavljanje novog, izmenjenog doktora na sesiju
		session.setAttribute("currentUser", clinicAdmin);
		
		return new ResponseEntity<>(new ClinicAdminPersonalInformationDto(clinicAdmin), HttpStatus.OK);
	}

	/*
	 url: GET localhost:8081/theGoodShepherd/clinicAdmin
	 HTTP request for viewing clinic administrators
	 returns ResponseEntity object
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClinicAdminDto>> getAllClinicAdmins() {
		
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can view  all clinic administrators.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<ClinicAdmin> clinicAdmins = adminService.findAll();
		List<ClinicAdminDto> dto = new ArrayList<ClinicAdminDto>();
		
		for(ClinicAdmin ca : clinicAdmins) {
			dto.add(new ClinicAdminDto(ca, ca.getClinic().getName()));
		}
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/*
	 url: POST localhost:8081/theGoodShepherd/clinicAdmin/addNewClinicAdmin/{clinicId}
	 HTTP request for adding new clinic administrator
	 receives ClinicAdmin object
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/addNewClinicAdmin/{clinicId}",
				 consumes = MediaType.APPLICATION_JSON_VALUE, 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicAdminDto> createClinicAdmin(@RequestBody ClinicAdmin clinicAdmin, @PathVariable String clinicId) {
		
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can add new clinical center administrators.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		RegisteredUser user = userService.findOneByEmail(clinicAdmin.getEmail());
		if (user != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with given email already exists!");
		}
		
		user = userService.findOneBySecurityNumber(clinicAdmin.getSecurityNumber());
		if (user != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with given security number already exists!");
		}
		
		Clinic clinic = clinicService.findOneById(Long.parseLong(clinicId));
		
		if (clinic == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic with given name does not exist.");
		}
		
		clinic.add(clinicAdmin);
		clinicService.save(clinic);
		
		return new ResponseEntity<>(new ClinicAdminDto(clinicAdmin, clinic.getName()), HttpStatus.CREATED);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicAdmin/logIn/{email}/{password}
	 HTTP request for checking email and password
	 receives String email and String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "logIn/{email}/{password}")
	public ResponseEntity<RegisteredUserDto> logIn(@PathVariable String email, @PathVariable String password){
		
		if (session.getAttribute("currentUser") != null) {
			// postoji ulogovani korisnik
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already loged in!");
		}
		
		ClinicAdmin clinicAdmin = adminService.findOneByEmail(email);
		if(clinicAdmin == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic administrator with given email does not exist.");
			/*HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("400", "Bad Request");
			
			return new ResponseEntity<ClinicAdmin>(null, responseHeaders, HttpStatus.BAD_REQUEST);*/
		}
		
		if(!clinicAdmin.getPassword().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password do not match!");
		}
		// postavlja trenutno ulogovanog na sesiju
		session.setAttribute("currentUser", clinicAdmin);
		
		//treba da vraca clinicAdmin
		return new ResponseEntity<>(new RegisteredUserDto(clinicAdmin, clinicAdmin.getLogged()), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/clinicAdmin/getDoctors
	 HTTP request for viewing doctors in clinic admins clinic
	 returns ResponseEntity object
	 */
	@GetMapping(value = "getDoctors" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DoctorDto>> getDoctors() {
		
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can view  all clinic administrators.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Doctor> doctors = doctorService.findAllByClinicId(currentUser.getClinic().getId());
		List<DoctorDto> dto = new ArrayList<DoctorDto>();
		
		for(Doctor d : doctors) {
			dto.add(new DoctorDto(d));
		}
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 * url: POST localhost:8081/theGoodShepherd/clinicAdmin/acceptAppointmentRequest
	 * HTTP request for sending an acceptance email to patient
	 * receives: AppointmentRequest instance
	 * returns: String instance
	 * */
	@PostMapping(value = "/acceptAppointmentRequest" ,
			consumes = MediaType.APPLICATION_JSON_VALUE //-> {"id" : 2, "appointment" :{ "ordination" :{ "name" : "Ordination1" }}} npr
			)
	public void acceptAppointmentRequest(@RequestBody AppointmentReqDto appointmentRequest){
		//proveriti da li je termin slobodan
			//ako jeste posalji mejl korisniku da je odobreno
			//ako nije, pronadji prvi slobodni termin i posalji koristiku te detalje
		if(session.getAttribute("currentUser") == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		ClinicAdmin currentAdmin = (ClinicAdmin) session.getAttribute("currentUser");
		AppointmentRequest appointmentReq = appointmentReqService.findOneById(appointmentRequest.getReqId());
		if(appointmentReq == null) { //mozda bi ovde trebalo proveriti da li je to neki koji je pre prihvacen/odbijen? mozda kasnije..
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment request doesn't exist!");
		}
		//postavi confirmed na true kod AppointmentRequest i Appointment
		appointmentReq.setApproved(true);
		appointmentReq.getAppointment().setDate(appointmentRequest.getDate());
		appointmentReq.getAppointment().setStartTime(appointmentRequest.getTime());
		appointmentReq.getAppointment().setEndTime(appointmentRequest.getTime() + appointmentReq.getAppointment().getAppType().getDuration());
		//dodati appointment doktoru, pacijentu i svima kojima treba
		Doctor doctor = doctorService.findOneByEmail(appointmentReq.getAppointment().getDoctor().getEmail());
		doctor.addAppointment(appointmentReq.getAppointment());
		Clinic clinic = clinicService.findOneById(appointmentReq.getClinic().getId());
		clinic.addAppointment(appointmentReq.getAppointment());
		
		try {
			ordinationService.update(appointmentRequest.getOrdId(), appointmentReq);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ordination with given id doesn't exist!");
		}
		
		//sacuvati sve u bazama
		doctorService.save(doctor);
		clinicService.save(clinic);
		appointmentService.save(appointmentReq.getAppointment());
		try {
			doctorService.sendDoctorNotificaitionAsync(currentAdmin, appointmentReq.getAppointment().getPatient(), 
					appointmentReq.getAppointment(), true, appointmentReq.getAppointment().getDoctor());
			adminService.sendNotificaitionAsync(currentAdmin, appointmentReq.getAppointment().getPatient(), 
					appointmentReq.getAppointment(), true, false, false);
		}catch( Exception e ){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*
	 * url: POST localhost:8081/theGoodShepherd/clinicAdmin/declineAppointmentRequest
	 * HTTP request for sending a rejection email to patient
	 * receives: AppointmentRequest instance
	 * returns: String instance
	 * */
	@PostMapping(value = "/declineAppointmentRequest" ,
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> declineAppointmentRequest(@RequestBody AppointmentRequest appointmentRequest){
		if (session.getAttribute("currentUser") == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user doesn't exist!");
		}
		ClinicAdmin currentAdmin = (ClinicAdmin) session.getAttribute("currentUser");
		AppointmentRequest appointmentReq = appointmentReqService.findOneById(appointmentRequest.getId());
		if(appointmentReq == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment request doesn't exist!");
		}
		appointmentReq.setApproved(false);
		appointmentReq.getAppointment().setConfirmed(false);
		try {
			adminService.sendNotificaitionAsync(currentAdmin, appointmentReq.getAppointment().getPatient(),null, false,false, false);
			return new ResponseEntity<>("Request rejected!", HttpStatus.OK);
		}catch( Exception e ){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*
	 * url: POST localhost:8081/theGoodShepherd/clinicAdmin/acceptOperationRequest
	 * HTTP request for sending an acceptance email to patient
	 * receives: AppointmentRequest instance
	 * returns: String instance
	 * */
	@PostMapping(value = "/acceptOperationRequest" ,
			consumes = MediaType.APPLICATION_JSON_VALUE //-> {"id" : 2, "appointment" :{ "ordination" :{ "name" : "Ordination1" }}} npr
			)
	public void acceptOperationRequest(@RequestBody AppointmentReqDto appointmentRequest){
		//proveriti da li je termin slobodan
			//ako jeste posalji mejl korisniku da je odobreno
			//ako nije, pronadji prvi slobodni termin i posalji koristiku te detalje
		if(session.getAttribute("currentUser") == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		ClinicAdmin currentAdmin = (ClinicAdmin) session.getAttribute("currentUser");
		AppointmentRequest appointmentReq = appointmentReqService.findOneById(appointmentRequest.getReqId());
		if(appointmentReq == null) { //mozda bi ovde trebalo proveriti da li je to neki koji je pre prihvacen/odbijen? mozda kasnije..
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment request doesn't exist!");
		}
		//postavi confirmed na true kod AppointmentRequest i Appointment
		appointmentReq.setApproved(true);
		appointmentReq.getAppointment().setConfirmed(true);
		appointmentReq.getAppointment().setDate(appointmentRequest.getDate());
		appointmentReq.getAppointment().setStartTime(appointmentRequest.getTime());
		appointmentReq.getAppointment().setEndTime(appointmentRequest.getTime() + appointmentReq.getAppointment().getAppType().getDuration());
		//dodati appointment doktoru, pacijentu i svima kojima treba
		for (Long d : appointmentRequest.getDoctors()) {
			Doctor doc = doctorService.findOneById(d);
			appointmentReq.getAppointment().addDoctor(doc);
			doc.addAppointment(appointmentReq.getAppointment());
			doctorService.save(doc);
		}
		Doctor doctor = doctorService.findOneByEmail(appointmentReq.getAppointment().getDoctor().getEmail());
		doctor.addAppointment(appointmentReq.getAppointment());
		Clinic clinic = clinicService.findOneById(appointmentReq.getClinic().getId());
		clinic.addAppointment(appointmentReq.getAppointment());
		
		try {
			ordinationService.update(appointmentRequest.getOrdId(), appointmentReq);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ordination with given id doesn't exist!");
		}
		
		//sacuvati sve u bazama
		doctorService.save(doctor);
		clinicService.save(clinic);
		appointmentService.save(appointmentReq.getAppointment());
		try {
			doctorService.sendNotificaitionAsync(currentAdmin, appointmentReq.getAppointment().getPatient(), 
					appointmentReq.getAppointment(), true, appointmentReq.getAppointment().getDoctors());
			adminService.sendNotificaitionAsync(currentAdmin, appointmentReq.getAppointment().getPatient(), 
					appointmentReq.getAppointment(), true, true, false);
		}catch( Exception e ){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}

package mrs.isa.team12.clinical.center.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.ClinicPatientDto;
import mrs.isa.team12.clinical.center.dto.MedicalRecordDto;
import mrs.isa.team12.clinical.center.dto.PatientDto;
import mrs.isa.team12.clinical.center.dto.PatientProfileDto;
import mrs.isa.team12.clinical.center.dto.PatientsDto;
import mrs.isa.team12.clinical.center.dto.RegisteredUserDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.Clinic;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.MedicalReport;
import mrs.isa.team12.clinical.center.model.Nurse;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.model.RegistrationRequest;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicAdminService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicalCenterAdminService;
import mrs.isa.team12.clinical.center.service.interfaces.MedicalReportService;
import mrs.isa.team12.clinical.center.service.interfaces.PatientService;
import mrs.isa.team12.clinical.center.service.interfaces.RegisteredUserService;
import mrs.isa.team12.clinical.center.service.interfaces.RegistrationRequestService;

@RestController
@RequestMapping("theGoodShepherd/patient")
public class PatientController {

	private PatientService patientService;
	private ClinicService clinicService;
	private ClinicalCenterAdminService centerAdminService;
	private AppointmentService appointmentService;
	private ClinicAdminService clinicAdminService;
	private RegisteredUserService userService;
	private RegistrationRequestService registrationService;
	private MedicalReportService medicalReportService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public PatientController(PatientService patientService,
			ClinicService clinicService, AppointmentService appointmentService, ClinicAdminService clinicAdminService,
			RegisteredUserService userService, RegistrationRequestService registrationService,
			ClinicalCenterAdminService centerAdminService, MedicalReportService medicalReportService) {
		this.patientService = patientService;
		this.clinicService = clinicService;
		this.centerAdminService = centerAdminService;
		this.appointmentService = appointmentService;
		this.clinicAdminService = clinicAdminService;
		this.userService = userService;
		this.registrationService = registrationService;
		this.medicalReportService = medicalReportService;
	}
	

	/*
	 url: GET localhost:8081/theGoodShepherd/patient
	 HTTP request for viewing registered patients
	 returns ResponseEntity object
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PatientsDto>> getAllPatients() {
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		Doctor currentUser = null;
		Nurse nurse = null;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			try {
				nurse = (Nurse) session.getAttribute("currentUser");
			} catch (ClassCastException ex) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctors and nurses can view registered patients");
			}
		}
		if (currentUser == null && nurse == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Patient> patients = patientService.findAll();
		List<PatientsDto> dto = new ArrayList<PatientsDto>();
		for(Patient p : patients) {
			dto.add(new PatientsDto(p));
		}
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/patient/filterPatients
	 HTTP request for filtering registered patients
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/filterPatients", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PatientsDto>> filterPatients(@RequestParam String name, @RequestParam String surname, @RequestParam String securityNumber) {

		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can view registered patients");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		List<Patient> patients = patientService.filter(name, surname, securityNumber);
		List<PatientsDto> dto = new ArrayList<PatientsDto>();
		for(Patient p : patients) {
			dto.add(new PatientsDto(p));
		}
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/patient/changePassword/{password}
	 HTTP request for changing password
	 receives String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "changePassword/{password}")
	public ResponseEntity<RegisteredUserDto> changePassword(@PathVariable String password) throws Exception{
		
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only patient can change his password.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Patient updated;
		try {
			updated = patientService.updatePassword(currentUser.getId(), password);
		}catch(NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with given id doesn't exist!");
		}
		
		session.setAttribute("currentUser", updated);
		
		return new ResponseEntity<>(new RegisteredUserDto(updated), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/patient/viewProfile
	 HTTP request for viewing logged in patient profile
	 receives String securityNumber
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/viewProfile")
	public ResponseEntity<PatientProfileDto> viewProfile(){
		
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a patient can view it's profile.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		currentUser = patientService.findOneByEmail(currentUser.getEmail());
		
		MedicalRecordDto medicalRecords = new MedicalRecordDto(currentUser.getMedicalRecords());
		// uzecemo sve preglede, ali cemo dodati samo one koji su finished!
		// da bismo izbegli dodavanje medical record i u appointment, vec da bude samo u pacijentu
		medicalRecords.setMedicalReports(currentUser.getAppointments());
		
		return new ResponseEntity<>(new PatientProfileDto(currentUser, medicalRecords), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/patient/editPersonalInformation
	 HTTP request for editing doctors personal information
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/editPersonalInformation")
	public ResponseEntity<PatientProfileDto> editPersonalInformation(@RequestBody PatientProfileDto editedProfile) throws Exception {

		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only patient can edit his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		if (!currentUser.getEmail().equals(editedProfile.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email can't be changed!");
		}
		
		editedProfile.setId(currentUser.getId());
				
		Patient patient;
		
		try {
			patient = patientService.update(editedProfile);
		}catch(NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient with given id doesn't exist!");
		}
		
		// postavljanje novog, izmenjenog pacijenta na sesiju
		session.setAttribute("currentUser", patient);
		
		MedicalRecordDto medicalRecords = new MedicalRecordDto(patient.getMedicalRecords());
		// uzecemo sve preglede, ali cemo dodati samo one koji su finished!
		// da bismo izbegli dodavanje medical record i u appointment, vec da bude samo u pacijentu
		medicalRecords.setMedicalReports(patient.getAppointments());
		
		return new ResponseEntity<>(new PatientProfileDto(patient, medicalRecords), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/patient/viewProfile/{secNum}
	 HTTP request for viewing chosen patient profile
	 receives String securityNumber
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/viewProfile/{secNum}")
	public ResponseEntity<PatientProfileDto> viewProfile(@PathVariable String secNum){
		
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can view registered patients");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Patient patient = patientService.findOneBySecurityNumber(secNum);
		List<Appointment> appointments = appointmentService.findAllByPatientIdAndDoctorId(patient.getId(), currentUser.getId());
		if(appointments.size() == 0) {
			return new ResponseEntity<>(new PatientProfileDto(patient), HttpStatus.OK);
		}
		// mogu biti i null, da nije do sad imala pregled!
		if (patient.getMedicalRecords() != null) {
			MedicalRecordDto medicalRecords = new MedicalRecordDto(patient.getMedicalRecords());
			// uzecemo sve preglede, ali cemo dodati samo one koji su finished!
			// da bismo izbegli dodavanje medical record i u appointment, vec da bude samo u pacijentu
			medicalRecords.setMedicalReports(patient.getAppointments());
			return new ResponseEntity<>(new PatientProfileDto(patient, medicalRecords), HttpStatus.OK);
		}
		// prvi pregled, nema records
		return new ResponseEntity<>(new PatientProfileDto(patient), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/patient/viewProfile/nurse/{secNum}
	 HTTP request for viewing chosen patient profile
	 receives String securityNumber
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/viewProfile/nurse/{secNum}")
	public ResponseEntity<PatientProfileDto> viewProfileNurse(@PathVariable String secNum){
		
		Nurse currentUser;
		try {
			currentUser = (Nurse) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only nurses can view registered patients");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Patient patient = patientService.findOneBySecurityNumber(secNum);
		List<MedicalReport> medicalReports = medicalReportService.findAllByNurseIdAndAppointmentPatientId(currentUser.getId(), patient.getId());
		if(medicalReports.size() == 0) {
			return new ResponseEntity<>(new PatientProfileDto(patient), HttpStatus.OK);
		}
		// if nisu null! mogu biti i null, da nije do sad imala pregled!
		if (patient.getMedicalRecords() != null) {
			MedicalRecordDto medicalRecords = new MedicalRecordDto(patient.getMedicalRecords());
			// uzecemo sve preglede, ali cemo dodati samo one koji su finished!
			// da bismo izbegli dodavanje medical record i u appointment, vec da bude samo u pacijentu
			medicalRecords.setMedicalReports(patient.getAppointments());
			return new ResponseEntity<>(new PatientProfileDto(patient, medicalRecords), HttpStatus.OK);
		}
		// prvi pregled, nema records
		return new ResponseEntity<>(new PatientProfileDto(patient), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/patient/logIn/{email}/{password}
	 HTTP request for checking email and password
	 receives String email and String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/logIn/{email}/{password}")
	public ResponseEntity<RegisteredUserDto> logIn(@PathVariable String email, @PathVariable String password){
		
		if (session.getAttribute("currentUser") != null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already loged in!");
		}
		
		Patient patient = patientService.findOneByEmail(email);
		
		if(patient == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient with given email does not exist.");
		}

		// nepotvrdjen zahtev za registraciju
		if(!patient.getRegistrationRequest().getApproved()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Registration request not approved yet!");
		}
		
		if(!patient.getPassword().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password do not match!");
		}
		
		session.setAttribute("currentUser", patient);
		
		return new ResponseEntity<>(new RegisteredUserDto(patient), HttpStatus.OK);
	}
	
	/*
	 * url: POST localhost:8081/theGoodShepherd/patient/register
	 * HTTP request for creating new patient profile
	 * receives: Patient instance
	 * returns: ReponseEntity instance
	 */
	
	@PostMapping(value = "/register",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientDto> registerPatient(@RequestBody Patient patient) throws Exception {
		// vec postoji ulogovani korisnik, ne moze se registrovati
		if (session.getAttribute("currentUser") != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already loged in!");
		}
		
		// provera da li postoji
		RegisteredUser existing = userService.findOneByEmail(patient.getEmail());
		if (existing != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with specified email already exists!");
		}
		existing = userService.findOneBySecurityNumber(patient.getSecurityNumber());
		if (existing != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with specified security number already exists!");
		}
		// ne postoji u bazi
		// sacuvamo ga
		RegistrationRequest regReq = new RegistrationRequest(patient, false, "");
		Patient saved = patientService.save(patient);
		registrationService.save(regReq);
		// dodavanje reference na registration request
		saved = patientService.update(saved, regReq);
		try {
			/*regReq = registrationService.save(new RegistrationRequest(patient, false, ""));
			System.out.println("---------------regreq id: "+regReq.getId());
			saved = patientService.update(saved, regReq);
			System.out.println("---------------regreq pacijenta: "+saved.getRegistrationRequest().getId());
			*/
			centerAdminService.sendNotificaitionAsync();
		}catch(NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration request with given id doesn't exist!");
		}
		return new ResponseEntity<>(new PatientDto(saved), HttpStatus.CREATED);
	}

	/*
	 * url: POST localhost:8081/theGoodShepherd/patient/sendAppointment
	 * HTTP request for sending an email to clinic admin(s)
	 * receives: Appointment instance
	 * returns: ReponseEntity instance
	 * */
	@PostMapping(value = "/sendAppointment",
				consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendAppointment(@RequestBody Appointment appointment) throws ParseException{
		
		if (session.getAttribute("currentUser") == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user doesn't exist!");
		}
		Patient currentPatient = (Patient) session.getAttribute("currentUser");

		try {
			//moramo svakom adminu klinike poslati mejl
			for (ClinicAdmin admin : clinicAdminService.findAllByClinicId(appointment.getClinic().getId())) {
				patientService.sendNotificaitionAsync(admin, currentPatient);
			}
		}catch( Exception e ){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/patient/clinics
	 HTTP request for viewing all existing clinics
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/clinics", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClinicPatientDto>> getAllClinics() {
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only patient can view  all clinics.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Clinic> clinics = clinicService.findAll();
		List<ClinicPatientDto> clinicsDto = new ArrayList<ClinicPatientDto>();
		
		for(Clinic c : clinics) {
			ClinicPatientDto clinic = new ClinicPatientDto(c);
			clinic.setAppointmentTypes(c.getAppointmentTypes());
			clinicsDto.add(clinic);
		}
		return new ResponseEntity<>(clinicsDto, HttpStatus.OK);
	}
}

package mrs.isa.team12.clinical.center.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

import mrs.isa.team12.clinical.center.dto.AppointmentTypeDto;
import mrs.isa.team12.clinical.center.dto.DoctorAppointmentTypesDto;
import mrs.isa.team12.clinical.center.dto.DoctorDto;
import mrs.isa.team12.clinical.center.dto.DoctorFreeTimesDto;
import mrs.isa.team12.clinical.center.dto.DoctorPersonalInformationDto;
import mrs.isa.team12.clinical.center.dto.DoctorsOrdinationsFreeTimesDto;
import mrs.isa.team12.clinical.center.dto.OrdinationFreeTimesDto;
import mrs.isa.team12.clinical.center.dto.RegisteredUserDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentRequestService;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentTypeService;
import mrs.isa.team12.clinical.center.service.interfaces.DoctorService;
import mrs.isa.team12.clinical.center.service.interfaces.OrdinationService;
import mrs.isa.team12.clinical.center.service.interfaces.RegisteredUserService;

@RestController
@RequestMapping("theGoodShepherd/doctor")
public class DoctorController {

	private DoctorService doctorService;
	private AppointmentTypeService appointmentTypeService;
	private RegisteredUserService userService;
	private OrdinationService ordinationService;
	private AppointmentRequestService appointmentReqService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public DoctorController(DoctorService doctorService, AppointmentTypeService appointmentTypeService, 
			RegisteredUserService userService, OrdinationService ordinationService, AppointmentRequestService appointmentReqService) {
		this.doctorService = doctorService;
		this.appointmentTypeService = appointmentTypeService;
		this.userService = userService;
		this.ordinationService = ordinationService;
		this.appointmentReqService = appointmentReqService;
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/doctor/logIn/{email}/{password}
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
		
		Doctor doctor = doctorService.findOneByEmail(email);
		
		if(doctor == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor with given email does not exist.");
		}
		
		if(!doctor.getPassword().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password do not match!");
		}
		
		session.setAttribute("currentUser", doctor);
		
		return new ResponseEntity<>(new RegisteredUserDto(doctor, doctor.getLogged()), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/doctor/changePassword/{password}
	 HTTP request for changing password
	 receives String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "changePassword/{password}")
	public ResponseEntity<RegisteredUserDto> changePassword(@PathVariable String password){
		
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can change his password.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Doctor updated = doctorService.updatePassword(currentUser.getId(), password);
		
		session.setAttribute("currentUser", updated);
		
		return new ResponseEntity<>(new RegisteredUserDto(updated), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/qualifications
	 HTTP request for doctors qualifications (appointment types he can perform)
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/qualifications",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DoctorAppointmentTypesDto> getQualifications() {
		
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can view all his qualifications.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Doctor doctor = doctorService.findOneById(currentUser.getId());

		DoctorAppointmentTypesDto dto = new DoctorAppointmentTypesDto(doctor.getId());
		Set<AppointmentType> appTypes = doctor.getAppointmentTypes();
		for(AppointmentType a : appTypes) {
			dto.addAppType(new AppointmentTypeDto(a));
		}
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/personalInformation
	 HTTP request for doctors personal information
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/personalInformation",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DoctorPersonalInformationDto> viewPersonalInformation() {
		
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can view his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Doctor doctor = doctorService.findOneById(currentUser.getId());

		DoctorPersonalInformationDto dto = new DoctorPersonalInformationDto(doctor);
		dto.setAppTypesSet(doctor.getAppointmentTypes());
		dto.setAllAppTypesSet(doctor.getClinic().getAppointmentTypes());
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/doctor/editPersonalInformation
	 HTTP request for editing doctors personal information
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/editPersonalInformation")
	public ResponseEntity<DoctorPersonalInformationDto> editPersonalInformation(@RequestBody DoctorPersonalInformationDto editedDoctor) {

		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can edit his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		if (!currentUser.getEmail().equals(editedDoctor.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email can't be changed!");
		}
		
		editedDoctor.setId(currentUser.getId());
		
		Set<AppointmentType> appTypes = appointmentTypeService.findAllByClinicIdAndNameIn(currentUser.getClinic().getId(), editedDoctor.getAppTypes());
		
		Doctor doctor = doctorService.update(editedDoctor, appTypes);
		
		// postavljanje novog, izmenjenog doktora na sesiju
		session.setAttribute("currentUser", doctor);
		
		return new ResponseEntity<>(new DoctorPersonalInformationDto(doctor), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/delete/{doctorId}
	 HTTP request for deleting doctor
	 parameter: String
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/delete/{doctorId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteDoctor(@PathVariable Long doctorId) {
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admins can remove doctors from their clinic.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Doctor doctor = doctorService.findOneById(doctorId);
		
		//provera da li doktor ima zakazane preglede
		//ukoliko ima zakazane preglede ne moze biti obrisan
		for(Appointment a : doctor.getAppointments()) {
			if(!a.getFinished()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Doctor with scheduled appointments cant be deleted!");
			}
		}

		doctorService.delete(doctor);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/getAll
	 HTTP request for viewing all doctors
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getAll",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DoctorDto>> getAllDoctors() {
		/*
		 * da
		 * li
		 * ce
		 * ovo
		 * ikome
		 * ikada
		 * trebati
		 * ?
		 */
		//mozda i nece
		List<Doctor> doctors = doctorService.findAll();
		List<DoctorDto> doctorsDto = new ArrayList<DoctorDto>();
		for(Doctor d : doctors) {
			doctorsDto.add(new DoctorDto(d));
		}
		return new ResponseEntity<>(doctorsDto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/availableDoctorOrdinations/{appDate}/{appType}
	 HTTP request for viewing available doctors and ordinations for given appointment type and date
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/availableDoctorOrdinations/{appDate}/{appType}" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DoctorsOrdinationsFreeTimesDto> availableDoctorOrdinations(@PathVariable("appDate") String appDate,
																	  	@PathVariable("appType") String appType) {
		
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can view available doctors and ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		// parsiranje datuma
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = new Date(dt.parse(appDate).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long clinicId = currentUser.getClinic().getId();
		
		AppointmentType type = appointmentTypeService.findOneByNameAndClinicId(appType, clinicId);
		List<Doctor> certifiedClinicDoctors = doctorService.findAllByClinicIdAndAppointmentTypes(clinicId, type);
		
		List<Ordination> clinicExamRooms = ordinationService.findAllByClinicIdAndType(clinicId, OrdinationType.ConsultingRoom);
		List<OrdinationFreeTimesDto> availableOrdinations = new ArrayList<OrdinationFreeTimesDto>();
		List<Integer> allAvailableExamRoomTimes = new ArrayList<Integer>();
		for(Ordination o : clinicExamRooms) {
			List<Integer> freeTimes = o.getAvailableTimesForDateAndType(date, type);
			if (!freeTimes.isEmpty()) {
				availableOrdinations.add(new OrdinationFreeTimesDto(o, freeTimes));
			}
			allAvailableExamRoomTimes.addAll(freeTimes);
		}
		
		List<DoctorFreeTimesDto> availableCertifiedClinicDoctors = new ArrayList<DoctorFreeTimesDto>();
		for (Doctor d : certifiedClinicDoctors) {
			List<Integer> freeTimes = d.getAvailableTimesForDateAndType(date, type);
			// da li ima slobodnog taj doktor kada je slobodna neka od ordinacija
			freeTimes.retainAll(allAvailableExamRoomTimes);
			if (!freeTimes.isEmpty()) {
				availableCertifiedClinicDoctors.add(new DoctorFreeTimesDto(d, type.getDuration(), type.getPrice(), freeTimes));
			}
		}
		
		return new ResponseEntity<>(new DoctorsOrdinationsFreeTimesDto(availableCertifiedClinicDoctors, availableOrdinations), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/certified/clinic/{appTypeName}/{appDate}/{clinicId}
	 HTTP request for getting all doctors with appointment type given by name
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/certified/clinic/{appTypeName}/{appDate}/{clinicId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DoctorFreeTimesDto>> getDoctorsByAppTypeName(@PathVariable("appTypeName") String appTypeName,
																@PathVariable("appDate") String appDate,
																@PathVariable("clinicId") Long clinicId) {
		
		Patient currentUser;
		try {
			currentUser = (Patient) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only patient can view certified doctors.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = new Date(dt.parse(appDate).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		AppointmentType type = appointmentTypeService.findOneByNameAndClinicId(appTypeName, clinicId);
		List<Doctor> certifiedClinicDoctors = doctorService.findAllByClinicIdAndAppointmentTypes(clinicId, type);
		
		List<DoctorFreeTimesDto> freeCertifiedClinicDoctors = new ArrayList<DoctorFreeTimesDto>();
		
		for (Doctor d : certifiedClinicDoctors) {
			List<Integer> freeTimes = d.getAvailableTimesForDateAndType(date, type);
			// da li ima slobodnog taj doktor
			if (!freeTimes.isEmpty()) {
				freeCertifiedClinicDoctors.add(new DoctorFreeTimesDto(d, freeTimes));
			}
		}
		
		return new ResponseEntity<>(freeCertifiedClinicDoctors, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/certified/operations/{operReqId}
	 HTTP request for getting all doctors with appointment type given by name
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/certified/operations/{operReqId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DoctorDto>> getDoctorsForOperation(@PathVariable("operReqId") Long operReqId) {
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admin can view certified doctors.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		AppointmentRequest ar = appointmentReqService.findOneById(operReqId);
		if (ar == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Operation request with given id doesn't exist!");
		}
		
		AppointmentType type = appointmentTypeService.findOneByNameAndClinicId(ar.getAppointment().getAppType().getName(), ar.getClinic().getId());
		List<Doctor> certifiedClinicDoctors = doctorService.findAllByClinicIdAndAppointmentTypes(ar.getClinic().getId(), type);
		
		List<DoctorDto> freeCertifiedClinicDoctors = new ArrayList<DoctorDto>();
		
		for (Doctor d : certifiedClinicDoctors) {
			if(!d.getEmail().equals(ar.getAppointment().getDoctor().getEmail())) {
				freeCertifiedClinicDoctors.add(new DoctorDto(d));
			}
		}
		
		return new ResponseEntity<>(freeCertifiedClinicDoctors, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/findOne/{id}
	 HTTP request for getting doctor from database with given id
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/findOne/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public DoctorDto getDoctorById(@PathVariable("id") Long id) {
		/*
		 * da
		 * li
		 * ce
		 * nekome
		 * nekad
		 * ovo
		 * trebati
		 * ?
		 */
		// ko koristi ovo od korisnika, ko ima prava?
		return new DoctorDto(doctorService.findOneById(id));
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/doctor/addNewDoctor
	 HTTP request for adding new doctor
	 receives Doctor object doctor
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/addNewDoctor")
	public ResponseEntity<DoctorDto> addNewDoctor(@RequestBody Doctor doctor) {
		
		ClinicAdmin admin;
		try {
			admin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can add new doctors.");
		}
		if (admin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		RegisteredUser existing = userService.findOneByEmail(doctor.getEmail());
		if (existing != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with specified email already exists!");
		}
		existing = userService.findOneBySecurityNumber(doctor.getSecurityNumber());
		if (existing != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with specified security number already exists!");
		}
		// ne postoji u bazi
		// sacuvamo ga
		doctor.setClinic(admin.getClinic());
		doctor.setActive(true);
		Doctor saved = doctorService.save(doctor);
		
		return new ResponseEntity<>(new DoctorDto(saved), HttpStatus.CREATED);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/doctor/getFreeTimes/{appTypeId}/{appDate}
	 HTTP request for getting available times of a doctor for specified date and appointment type
	 receives Long appTypeId, String date
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/getFreeTimes/{appTypeId}/{appDate}")
	public ResponseEntity<List<Integer>> getFreeTimes(@PathVariable("appTypeId") Long appTypeId,
												  @PathVariable("appDate") String appDate) {
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can add new doctors.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		// parsiranje datuma
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = new Date(dt.parse(appDate).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		AppointmentType appType = appointmentTypeService.findOneById(appTypeId);
		Doctor doctor = doctorService.findOneById(currentUser.getId());
		List<Integer> freeTimes = doctor.getAvailableTimesForDateAndType(date, appType);
		
		return new ResponseEntity<>(freeTimes, HttpStatus.CREATED);
	}
	
	// FILTRIRANJE DOKTORA NA BEKU
	// MOZDA ZATREBA
	/*
	 url: GET localhost:8081/theGoodShepherd/doctor/filterDoctors
	 HTTP request for filtering clinic doctors
	 returns ResponseEntity object
	 */
	/*@GetMapping(value = "/filterPatients", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ViewPatientsDto>> filterPatients(@RequestParam String name, @RequestParam String surname, @RequestParam String securityNumber) {

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
		List<ViewPatientsDto> dto = new ArrayList<ViewPatientsDto>();
		for(Patient p : patients) {
			dto.add(new ViewPatientsDto(p));
		}
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}*/
	
}

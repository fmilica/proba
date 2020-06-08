package mrs.isa.team12.clinical.center.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.ClinicalCentreAdminDto;
import mrs.isa.team12.clinical.center.dto.ClinicalCentreAdminPersonalInformationDto;
import mrs.isa.team12.clinical.center.dto.RegisteredUserDto;
import mrs.isa.team12.clinical.center.model.ClinicalCentre;
import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;
import mrs.isa.team12.clinical.center.model.Patient;
import mrs.isa.team12.clinical.center.model.RegisteredUser;
import mrs.isa.team12.clinical.center.model.RegistrationRequest;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicalCenterAdminService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicalCenterService;
import mrs.isa.team12.clinical.center.service.interfaces.PatientService;
import mrs.isa.team12.clinical.center.service.interfaces.RegisteredUserService;
import mrs.isa.team12.clinical.center.service.interfaces.RegistrationRequestService;

@RestController
@RequestMapping("theGoodShepherd/clinicalCenterAdmin")
public class ClinicalCenterAdminController {

	private ClinicalCenterAdminService clinicalCenterAdminService;
	private ClinicalCenterService centreService;
	private RegistrationRequestService registrationService;
	private PatientService patientService;
	private RegisteredUserService userService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	public ClinicalCenterAdminController(ClinicalCenterAdminService clinicalCenterAdminService, ClinicalCenterService centreService, 
			RegistrationRequestService registrationService, PatientService patientService, RegisteredUserService userService) {
		this.clinicalCenterAdminService = clinicalCenterAdminService;
		this.centreService = centreService;
		this.registrationService = registrationService;
		this.patientService = patientService;
		this.userService = userService;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/clinicalCenterAdmin
	 HTTP request for viewing clinical center administrators
	 returns ResponseEntity object
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClinicalCentreAdminDto>> getAllClinicalCentreAdmins() {

		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can view  all clinical center administrators.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<ClinicalCentreAdminDto> dtos = new ArrayList<ClinicalCentreAdminDto>();
		
		List<ClinicalCentreAdmin> clinicalCenreAdmins = clinicalCenterAdminService.findAll();
		for(ClinicalCentreAdmin cca: clinicalCenreAdmins) {
			dtos.add(new ClinicalCentreAdminDto(cca));
		}
		
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicalCenterAdmin/logIn/{email}/{password}
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
		
		ClinicalCentreAdmin clinicalCentreAdmin = clinicalCenterAdminService.findOneByEmail(email);
		
		if(clinicalCentreAdmin == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinical center administrator with given email does not exist.");
		}
		
		if(!clinicalCentreAdmin.getPassword().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password do not match!");
		}
		
		session.setAttribute("currentUser", clinicalCentreAdmin);
		
		return new ResponseEntity<>(new RegisteredUserDto(clinicalCentreAdmin, clinicalCentreAdmin.getLogged()), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicalCenterAdmin/changePassword/{password}
	 HTTP request for changing password
	 receives String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "changePassword/{password}")
	public ResponseEntity<RegisteredUserDto> changePassword(@PathVariable String password){
		
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical centre admin can change his password.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		ClinicalCentreAdmin updated = clinicalCenterAdminService.updatePassword(currentUser.getId(), password);
		
		session.setAttribute("currentUser", updated);
		
		return new ResponseEntity<>(new RegisteredUserDto(updated), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/clinicalCenterAdmin/personalInformation
	 HTTP request for clinical centre admin personal information
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/personalInformation",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicalCentreAdminPersonalInformationDto> viewPersonalInformation() {
		
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical centre admin can view his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		ClinicalCentreAdmin clinicalCentreAdmin = clinicalCenterAdminService.findOneById(currentUser.getId());

		return new ResponseEntity<>(new ClinicalCentreAdminPersonalInformationDto(clinicalCentreAdmin), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicalCenterAdmin/editPersonalInformation
	 HTTP request for editing clinical centre admin personal information
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/editPersonalInformation")
	public ResponseEntity<ClinicalCentreAdminPersonalInformationDto> editPersonalInformation(@RequestBody ClinicalCentreAdminPersonalInformationDto editedProfile) {

		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical centre admin can edit his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		if (!currentUser.getEmail().equals(editedProfile.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email can't be changed!");
		}
		
		editedProfile.setId(currentUser.getId());
			
		ClinicalCentreAdmin clinicalCentreAdmin = clinicalCenterAdminService.update(editedProfile);
		
		// postavljanje novog, izmenjenog doktora na sesiju
		session.setAttribute("currentUser", clinicalCentreAdmin);
		
		return new ResponseEntity<>(new ClinicalCentreAdminPersonalInformationDto(clinicalCentreAdmin), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicalCenterAdmin/addNewClinicalCentreAdmin
	 HTTP request for adding new clinic administrator
	 receives ClinicAdmin object
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/addNewClinicalCentreAdmin",
			 consumes = MediaType.APPLICATION_JSON_VALUE, 
			 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClinicalCentreAdminDto> createClinicalCentreAdmin(@RequestBody ClinicalCentreAdmin clinicalCentreAdmin) {
		
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can add new clinical center administrators.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}

		RegisteredUser user = userService.findOneByEmail(clinicalCentreAdmin.getEmail());
		if (user != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with given email already exists!");
		}
		
		user = userService.findOneBySecurityNumber(clinicalCentreAdmin.getSecurityNumber());
		if (user != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with given security number already exists!");
		}
		
		ClinicalCentre clinicalCentre = centreService.findOneByName(currentUser.getClinicalCentre().getName());
		if(clinicalCentre == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinical centre with given name does not exist.");
		}
		
		clinicalCentre.add(clinicalCentreAdmin);
		
		clinicalCentreAdmin.setClinicalCentre(clinicalCentre);
		clinicalCenterAdminService.save(clinicalCentreAdmin);
		ClinicalCentreAdminDto dto = new ClinicalCentreAdminDto(clinicalCentreAdmin);
		//centreService.save(clinicalCentre);
		
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicalCenterAdmin/acceptRegistrationRequest
	 HTTP request for accepting registration request
	 receives RegistrationRequest object
	 */
	@PostMapping(value = "/acceptRegistrationRequest",
			 consumes = MediaType.APPLICATION_JSON_VALUE)
	public void acceptRegistrationRequest(@RequestBody RegistrationRequest regReq) {
		
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can accept new registration requests.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		RegistrationRequest registrationRequest;
		try {
			registrationRequest = registrationService.update(regReq.getId(), true);
			
			Patient user = patientService.findOneById(registrationRequest.getUser().getId());
			
			clinicalCenterAdminService.sendNotificaitionAsync(currentUser, user, registrationRequest.getDescription(), true);
		}catch(NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data with requested id doesn't exist!");
		}catch( Exception e ){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	
	/*
	 url: POST localhost:8081/theGoodShepherd/clinicalCenterAdmin/declineRegistrationRequest
	 HTTP request for declining a registration request
	 receives RegistrationRequest object
	 */
	@PostMapping(value = "/declineRegistrationRequest",
			 consumes = MediaType.APPLICATION_JSON_VALUE)
	public void declineRegistrationRequest(@RequestBody RegistrationRequest regReq) {
		
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can decline new registration requests.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		//pokusam da obrisem samo patient i nadam se da ce pobrisati i ostalo
		try {
			Patient user = patientService.deleteByRequestId(regReq.getId());
			clinicalCenterAdminService.sendNotificaitionAsync(currentUser, user, regReq.getDescription(), false);
		}catch( NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient with requested id doesn't exist!");
		}catch( Exception e ){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
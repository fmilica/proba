package mrs.isa.team12.clinical.center.controller;

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

import mrs.isa.team12.clinical.center.dto.NursePersonalInformationDto;
import mrs.isa.team12.clinical.center.dto.RegisteredUserDto;
import mrs.isa.team12.clinical.center.model.Nurse;
import mrs.isa.team12.clinical.center.service.NurseImpl;

@RestController
@RequestMapping("theGoodShepherd/nurse")
public class NurseController {

	private NurseImpl nurseService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	public NurseController(NurseImpl nurseService) {
		this.nurseService = nurseService;
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/nurse/logIn/{email}/{password}
	 HTTP request for checking email and password
	 receives String email and String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "logIn/{email}/{password}")
	public ResponseEntity<RegisteredUserDto> logIn(@PathVariable String email, @PathVariable String password){
		
		if (session.getAttribute("currentUser") != null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already loged in!");
		}
		
		Nurse nurse = nurseService.findOneByEmail(email);
		
		if(nurse == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nurse with given email does not exist.");
		}
		
		if(!nurse.getPassword().equals(password)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password do not match!");
		}
		
		session.setAttribute("currentUser", nurse);
		
		return new ResponseEntity<>(new RegisteredUserDto(nurse), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/nurse/changePassword/{password}
	 HTTP request for changing password
	 receives String password
	 returns ResponseEntity object
	 */
	@PostMapping(value = "changePassword/{password}")
	public ResponseEntity<RegisteredUserDto> changePassword(@PathVariable String password){
		
		Nurse currentUser;
		try {
			currentUser = (Nurse) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only nurse can change her password.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Nurse updated = nurseService.updatePassword(currentUser.getId(), password);
		
		session.setAttribute("currentUser", updated);
		
		return new ResponseEntity<>(new RegisteredUserDto(updated), HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/nurse/personalInformation
	 HTTP request for nurse personal information
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/personalInformation",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NursePersonalInformationDto> viewPersonalInformation() {
		
		Nurse currentUser;
		try {
			currentUser = (Nurse) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only nurse can view her personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Nurse nurse = nurseService.findOneById(currentUser.getId());

		NursePersonalInformationDto dto = new NursePersonalInformationDto(nurse);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/nurse/editPersonalInformation
	 HTTP request for editing nurses personal information
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/editPersonalInformation")
	public ResponseEntity<NursePersonalInformationDto> editPersonalInformation(@RequestBody NursePersonalInformationDto editedProfile) {

		Nurse currentUser;
		try {
			currentUser = (Nurse) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only nurse can edit his personal information.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		if (!currentUser.getEmail().equals(editedProfile.getEmail())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email can't be changed!");
		}
		
		editedProfile.setId(currentUser.getId());
		
		Nurse nurse = nurseService.update(editedProfile);
		
		// postavljanje novog, izmenjenog doktora na sesiju
		session.setAttribute("currentUser", nurse);
		
		return new ResponseEntity<>(new NursePersonalInformationDto(nurse), HttpStatus.OK);
	}
	
}

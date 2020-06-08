package mrs.isa.team12.clinical.center.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.RegisteredUserDto;
import mrs.isa.team12.clinical.center.model.RegisteredUser;

@RestController
@RequestMapping("theGoodShepherd/loggedUser")
public class LoggedUserController {
	
	@Autowired HttpSession session;

	/*
	 url: GET localhost:8081/theGoodShepherd/loggedUser
	 HTTP request for checking if logged user is there and if its their first login
	 returns ResponseEntity
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredUserDto> loggedUser() {
		RegisteredUser currentUser = (RegisteredUser) session.getAttribute("currentUser");
		
		if (currentUser == null) {
			session.invalidate();
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user logged in!");
		}else {
			return new ResponseEntity<>(new RegisteredUserDto(currentUser, currentUser.getLogged()), HttpStatus.OK);
		}
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/loggedUser
	 HTTP request for checking if logged user is there and if its their first login
	 returns ResponseEntity
	 */
	@GetMapping(value="/current", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisteredUserDto> loggedUserCurrent() {
		RegisteredUser currentUser = (RegisteredUser) session.getAttribute("currentUser");
		
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user logged in!");
		}else {
			return new ResponseEntity<>(new RegisteredUserDto(currentUser, currentUser.getLogged()), HttpStatus.OK);
		}
	}
}

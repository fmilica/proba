package mrs.isa.team12.clinical.center.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("theGoodShepherd/logOut")
public class LogOutController {
	
	@Autowired HttpSession session;

	/*
	 url: GET localhost:8081/theGoodShepherd/logOut
	 HTTP request for logging out
	 returns ResponseEntity
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public void logOut() {
		
		if(session.getAttribute("currentUser") == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user logged in!");
		}
		
		session.invalidate();
	}
}

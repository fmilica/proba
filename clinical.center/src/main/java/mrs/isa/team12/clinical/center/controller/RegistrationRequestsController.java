package mrs.isa.team12.clinical.center.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.RegistrationRequestDto;
import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;
import mrs.isa.team12.clinical.center.model.RegistrationRequest;
import mrs.isa.team12.clinical.center.service.interfaces.RegistrationRequestService;

@RestController
@RequestMapping("theGoodShepherd/registrationReq")
public class RegistrationRequestsController {

	private RegistrationRequestService registrationReqService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public RegistrationRequestsController(RegistrationRequestService registrationReq) {
		this.registrationReqService = registrationReq;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/registrationReq
	 HTTP request for viewing registration requests
	 returns ResponseEntity object
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RegistrationRequestDto>> registrationReq() {
		
		// da li je neko ulogovan
		// da li je odgovarajuceg tipa
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can view  all registration requests.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		List<RegistrationRequestDto> rrdtos = new ArrayList<RegistrationRequestDto>();
		List<RegistrationRequest> registrationReqs = registrationReqService.findAll();
		
		for (RegistrationRequest registrationRequest : registrationReqs) {
			if(registrationRequest.getApproved() == false) {
				rrdtos.add(new RegistrationRequestDto(registrationRequest));
			}
		}
		
		return new ResponseEntity<>(rrdtos, HttpStatus.OK);
	}
}

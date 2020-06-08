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

import mrs.isa.team12.clinical.center.dto.AppointmentRequestDto;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentRequestService;

@RestController
@RequestMapping("theGoodShepherd/appointmentRequest")
public class AppointmentRequestController {

	private AppointmentRequestService appointmentRequestService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public AppointmentRequestController(AppointmentRequestService appointmentRequestService) {
		this.appointmentRequestService = appointmentRequestService;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointmentRequest
	 HTTP request for getting all appointment requests from one clinic
	 returns ResponseEntity object
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentRequestDto>> getAllClinicAppReqs() {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical administrators can view all appointment requesets.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<AppointmentRequest> reqs = appointmentRequestService.findAllByClinicAndApproved(currentUser.getClinic(), false);
		List<AppointmentRequestDto> dto = new ArrayList<AppointmentRequestDto>();
		for(AppointmentRequest ar : reqs) {
			if (!ar.getApproved()) {
				dto.add(new AppointmentRequestDto(ar));
			}
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointmentRequest/operations
	 HTTP request for getting all appointment requests from one clinic
	 returns ResponseEntity object
	 */
	@GetMapping(value="/operations",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AppointmentRequestDto>> getOperationReqs() {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical administrators can view all appointment requesets.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<AppointmentRequest> reqs = appointmentRequestService.findAllByClinicAndApproved(currentUser.getClinic(), false);
		List<AppointmentRequestDto> dto = new ArrayList<AppointmentRequestDto>();
		for(AppointmentRequest ar : reqs) {
			if (!ar.getApproved() && ar.getAppointment().getType() == OrdinationType.OperatingRoom) {
				dto.add(new AppointmentRequestDto(ar));
			}
		}
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
}

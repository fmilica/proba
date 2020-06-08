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

import mrs.isa.team12.clinical.center.dto.DiagnosisDto;
import mrs.isa.team12.clinical.center.dto.DiagnosisPrescriptionDto;
import mrs.isa.team12.clinical.center.model.ClinicalCentreAdmin;
import mrs.isa.team12.clinical.center.model.DiagnosePerscription;
import mrs.isa.team12.clinical.center.model.Diagnosis;
import mrs.isa.team12.clinical.center.service.interfaces.DiagnosisPrescriptionService;

@RestController
@RequestMapping("/theGoodShepherd/diagnosePrescription")
public class DiagnosePrescriptionController {

	private DiagnosisPrescriptionService diagnosisPrescService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public DiagnosePrescriptionController(DiagnosisPrescriptionService dps) {
		this.diagnosisPrescService = dps;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/diagnosePrescription
	 HTTP request for viewing all diagnosis
	 returns ResponseEntity object
	 */
	@GetMapping(value = "",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DiagnosisPrescriptionDto>> getAllDisagnosis() {
		
		ClinicalCentreAdmin currentUser;
		try {
			currentUser = (ClinicalCentreAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinical center administrators can view  all diagnosis.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<DiagnosePerscription> diagnosePrescription = diagnosisPrescService.findAll();
		
		List<DiagnosisPrescriptionDto> diagnosisPrescDto = new ArrayList<DiagnosisPrescriptionDto>();
		for (DiagnosePerscription dp : diagnosePrescription) {
			diagnosisPrescDto.add(new DiagnosisPrescriptionDto(dp));
		}
		
		return new ResponseEntity<>(diagnosisPrescDto, HttpStatus.OK);
	}
}

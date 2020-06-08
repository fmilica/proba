package mrs.isa.team12.clinical.center.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.MedicalRecordDto;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.MedicalRecords;
import mrs.isa.team12.clinical.center.service.interfaces.MedicalRecordsService;

@RestController
@RequestMapping("theGoodShepherd/medicalRecord")
public class MedicalRecordController {
	
	private MedicalRecordsService medicalRecordsService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public MedicalRecordController(MedicalRecordsService mrs) {
		this.medicalRecordsService = mrs;
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/medicalRecord/{id}
	 HTTP request for viewing medical record
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/{id}")
	public ResponseEntity<MedicalRecordDto> getOneMedicalRecord(@PathVariable String id) {
		Doctor currentUser;
		try {
			currentUser = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctor can view medical records");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		MedicalRecords medicalRecord = medicalRecordsService.findOneById(Long.parseLong(id));
		
		MedicalRecordDto mrd = new MedicalRecordDto(medicalRecord);
		
		return new ResponseEntity<>(mrd, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/medicalRecord/modifyMedicalRecord
	 HTTP request for modifyig medical record
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/modifyMedicalRecord")
	public ResponseEntity<MedicalRecordDto> addNewMedicalReport(@RequestBody MedicalRecordDto medicalRecordDto){
		
		Doctor doctor;
		try {
			doctor = (Doctor) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctors can add new medical reports.");
		}
		if (doctor == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		MedicalRecords medicalRecord = medicalRecordsService.findOneById(medicalRecordDto.getId());
		
		if(medicalRecord == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment doesn't exist!");
		}
		
		medicalRecord.setHeight(medicalRecordDto.getHeight());
		medicalRecord.setWeight(medicalRecordDto.getWeight());
		medicalRecord.setBloodPressure(medicalRecordDto.getBloodPressure());
		medicalRecord.setBloodType(medicalRecordDto.getBloodType());
		medicalRecord.setAllergies(medicalRecordDto.getAllergies());
		
		medicalRecordsService.save(medicalRecord);
		
		medicalRecordDto = new MedicalRecordDto(medicalRecord);
		
		return new ResponseEntity<>(medicalRecordDto, HttpStatus.CREATED);
	}
}

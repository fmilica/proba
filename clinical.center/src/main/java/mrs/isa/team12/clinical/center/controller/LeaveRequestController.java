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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mrs.isa.team12.clinical.center.dto.LeaveDto;
import mrs.isa.team12.clinical.center.dto.LeaveRequestDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Leave;
import mrs.isa.team12.clinical.center.model.LeaveRequest;
import mrs.isa.team12.clinical.center.model.MedicalPersonnel;
import mrs.isa.team12.clinical.center.model.Nurse;
import mrs.isa.team12.clinical.center.model.enums.LeaveType;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentService;
import mrs.isa.team12.clinical.center.service.interfaces.ClinicAdminService;
import mrs.isa.team12.clinical.center.service.interfaces.LeaveRequestService;

@RestController
@RequestMapping("theGoodShepherd/leaveRequest")
public class LeaveRequestController {
	
	private LeaveRequestService leaveRequestService;
	private AppointmentService appointmentService;
	private ClinicAdminService clinicAdminService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public LeaveRequestController(LeaveRequestService lrs, AppointmentService as, ClinicAdminService cas) {
		this.leaveRequestService = lrs;
		this.appointmentService = as;
		this.clinicAdminService = cas;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/leaveRequest
	 HTTP request for getting all leave requests of current user
	 returns ResponseEntity object
	*/
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LeaveRequestDto>> getAllLeaveRequests() {
		Nurse nurse = null;
		Doctor doctor = null;
		Long id;
		try {
			nurse = (Nurse) session.getAttribute("currentUser");
			id = nurse.getId();
		} catch (ClassCastException e) {
			try {
				doctor = (Doctor) session.getAttribute("currentUser");
				id = doctor.getId();
			} catch (ClassCastException ex) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctors can view their leave requests.");
			}
		}
		if (nurse == null && doctor == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<LeaveRequest> lr = leaveRequestService.findAllByLeaveMedicalPersoneId(id);
		List<LeaveRequestDto> lrDtos = new ArrayList<LeaveRequestDto>();
		for (LeaveRequest leaveRequest : lr) {
			lrDtos.add(new LeaveRequestDto(leaveRequest));
		}
		
		return new ResponseEntity<>(lrDtos, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/leaveRequest
	 HTTP request for getting all leave requests of current user
	 returns ResponseEntity object
	*/
	@PostMapping(value = "/addNewLeaveRequest",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addNewLeaveRequest(@RequestBody LeaveDto leave) {
		Nurse nurse = null;
		Doctor doctor = null;
		MedicalPersonnel mp = null;
		try {
			nurse = (Nurse) session.getAttribute("currentUser");
			mp = nurse;
		} catch (ClassCastException e) {
			try {
				doctor = (Doctor) session.getAttribute("currentUser");
				mp = doctor;
			} catch (ClassCastException ex) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctors can view their leave requests.");
			}
		}
		if (nurse == null && doctor == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		LeaveType lt = leave.getType().equals("Paid") ? LeaveType.Paid : LeaveType.Vacation;
		
		if(nurse == null) {
			List<Appointment> appointments = appointmentService.findAllByDoctorIdAndDateBetween(mp.getId(), leave.getStartDate(), leave.getEndDate());
			
			if(appointments.size() == 0) {
				LeaveRequest lr = new LeaveRequest(new Leave(leave.getStartDate(), leave.getEndDate(), lt, mp), false, "");
				leaveRequestService.save(lr);
			}else if(!leave.getType().equals("Paid")){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't request a vacation if you have appointments in that period.");
			}
		}else {
			LeaveRequest lr = new LeaveRequest(new Leave(leave.getStartDate(), leave.getEndDate(), lt, mp), false, "");
			leaveRequestService.save(lr);
		}
		
		//slanje mejla svim adminima klinike
		clinicAdminService.sendLeaveRequestNotification(mp.getClinic().getId());
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/leaveRequest/currentAdmin
	 HTTP request for getting all leave requests of current user's clinic
	 returns ResponseEntity object
	*/
	@GetMapping(value="/currentAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LeaveRequestDto>> getAllLR() {
		ClinicAdmin clinicAdmin;
		try {
			clinicAdmin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admins can view leave requests in their clinic.");
		}
		if (clinicAdmin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<LeaveRequest> lr = leaveRequestService.findAllByLeaveMedicalPersoneClinicId(clinicAdmin.getClinic().getId());
		List<LeaveRequestDto> lrDtos = new ArrayList<LeaveRequestDto>();
		for (LeaveRequest leaveRequest : lr) {
			if(!leaveRequest.getApproved()){
				lrDtos.add(new LeaveRequestDto(leaveRequest));
			}
		}
		
		return new ResponseEntity<>(lrDtos, HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/leaveRequest/acceptLeaveRequest
	 HTTP request for accepting leave request
	 returns ResponseEntity object
	*/
	@PostMapping(value = "/acceptLeaveRequest",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void acceptLeaveRequest(@RequestBody LeaveRequestDto leaveReq) throws Exception {
		ClinicAdmin clinicAdmin;
		try {
			clinicAdmin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admins can accept leave request.");
		}
		if (clinicAdmin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		try {
			LeaveRequest lr = leaveRequestService.update(leaveReq.getId());
			if(lr.getLeave().getMedicalPersone() instanceof Doctor && lr.getLeave().getType() == LeaveType.Paid) {
				
				//izbrisati sve appointments gde je ovaj doktor....
				appointmentService.deleteAppointments((Doctor)lr.getLeave().getMedicalPersone(), lr);
			}
			
		}catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified leave request does not exist!");
		}
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/leaveRequest/declineLeaveRequest
	 HTTP request for accepting leave request
	 returns ResponseEntity object
	*/
	@PostMapping(value = "/declineLeaveRequest",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void declineLeaveRequest(@RequestBody LeaveRequestDto leaveReq) {
		ClinicAdmin clinicAdmin;
		try {
			clinicAdmin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only doctors can view their leave requests.");
		}
		if (clinicAdmin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		leaveRequestService.delete(leaveReq.getId(), leaveReq.getDescription());
		
	}
	
}

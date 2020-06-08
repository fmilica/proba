package mrs.isa.team12.clinical.center.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

import mrs.isa.team12.clinical.center.dto.ExamRoomDto;
import mrs.isa.team12.clinical.center.dto.OrdinationDto;
import mrs.isa.team12.clinical.center.model.Appointment;
import mrs.isa.team12.clinical.center.model.AppointmentRequest;
import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.ClinicAdmin;
import mrs.isa.team12.clinical.center.model.Doctor;
import mrs.isa.team12.clinical.center.model.Ordination;
import mrs.isa.team12.clinical.center.model.enums.OrdinationType;
import mrs.isa.team12.clinical.center.service.interfaces.AppointmentRequestService;
import mrs.isa.team12.clinical.center.service.interfaces.OrdinationService;

@RestController
@RequestMapping("theGoodShepherd/ordination")
public class OrdinationController {

	private OrdinationService ordinationService;
	private AppointmentRequestService appointmentRequestService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	public OrdinationController(OrdinationService ordinationService, 
			AppointmentRequestService appointmentRequestService) {
		this.ordinationService = ordinationService;
		this.appointmentRequestService = appointmentRequestService;
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getClinicsOrdinations
	 HTTP request for getting all ordinations from one clinic
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getClinicsOrdinations",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrdinationDto>> getClinicsOrdinations() {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view all ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Ordination> clinicsOrdins = ordinationService.findAllByClinicId(currentUser.getClinic().getId());
		List<OrdinationDto> clinicsOrdinsDtos = new ArrayList<OrdinationDto>();
		for(Ordination o : clinicsOrdins) {
			clinicsOrdinsDtos.add(new OrdinationDto(o));
		}
		return new ResponseEntity<>(clinicsOrdinsDtos, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getClinicsExamination
	 HTTP request for getting all ordinations from one clinic
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getClinicsExamination",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrdinationDto>> getClinicsExamination() {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view all ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Ordination> clinicsExamRooms = ordinationService.findAllByClinicIdAndType(currentUser.getClinic().getId(), OrdinationType.ConsultingRoom);
		List<OrdinationDto> clinicsExamRoomsDtos = new ArrayList<OrdinationDto>();
		for(Ordination o : clinicsExamRooms) {
			clinicsExamRoomsDtos.add(new OrdinationDto(o));
		}
		return new ResponseEntity<>(clinicsExamRoomsDtos, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getClinicsOrdinations/{name}
	 HTTP request for getting all exam rooms from one clinic with given name
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getClinicsExaminationName/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrdinationDto>> getClinicsOrdinationsFilter(@PathVariable("name") String name) {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view all ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Ordination> clinicsOrdins = ordinationService.findAllByClinicIdAndNameContainingIgnoreCaseAndType(currentUser.getClinic().getId(), name, OrdinationType.ConsultingRoom);
		List<OrdinationDto> clinicsOrdinsDtos = new ArrayList<OrdinationDto>();
		for(Ordination o : clinicsOrdins) {
			clinicsOrdinsDtos.add(new OrdinationDto(o));
		}
		return new ResponseEntity<>(clinicsOrdinsDtos, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getClinicsOrdinations/{number}
	 HTTP request for getting all exam rooms from one clinic with given number
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getClinicsExaminationNumber/{number}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrdinationDto>> getClinicsOrdinationsFilter(@PathVariable("number") Integer number) {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view all ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Ordination> clinicsOrdins = ordinationService.findAllByClinicIdAndOrdinationNumberAndType(currentUser.getClinic().getId(), number, OrdinationType.ConsultingRoom);
		List<OrdinationDto> clinicsOrdinsDtos = new ArrayList<OrdinationDto>();
		for(Ordination o : clinicsOrdins) {
			clinicsOrdinsDtos.add(new OrdinationDto(o));
		}
		return new ResponseEntity<>(clinicsOrdinsDtos, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getClinicsOrdinations/{name}/{number}
	 HTTP request for getting all exam rooms from one clinic with given name and number
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getClinicsExamination/{name}/{number}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrdinationDto>> getClinicsOrdinationsFilter(@PathVariable("name") String name,
																		@PathVariable("number") Integer number) {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view all ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		List<Ordination> clinicsOrdins = ordinationService.findAllByClinicIdAndNameContainingIgnoreCaseAndOrdinationNumberAndType(currentUser.getClinic().getId(), name, number, OrdinationType.ConsultingRoom);
		List<OrdinationDto> clinicsOrdinsDtos = new ArrayList<OrdinationDto>();
		for(Ordination o : clinicsOrdins) {
			clinicsOrdinsDtos.add(new OrdinationDto(o));
		}
		return new ResponseEntity<>(clinicsOrdinsDtos, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/viewOrdination/{ordinationId}
	 HTTP request for viewing specified ordination
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/viewOrdination/{ordinationId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrdinationDto> viewAppType(@PathVariable Long ordinationId) {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can edit ordinations.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Ordination ordination = ordinationService.findOneById(ordinationId);
		
		//provera da li ordinacija ima zakazane preglede
		//ukoliko ima zakazane preglede ne moze biti obrisan
		for(Appointment a : ordination.getAppointments()) {
			if(!a.getFinished()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ordiantion with scheduled appointments cant be edited!");
			}
		}
		return new ResponseEntity<>(new OrdinationDto(ordination), HttpStatus.OK);
	}
	
	/*
	 url: POST localhost:8081/theGoodShepherd/ordination/addNewOrdination/{name}/{type}
	 HTTP request for adding new ordination
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/addNewOrdination",
				 consumes = MediaType.APPLICATION_JSON_VALUE, 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrdinationDto> createOrdination(@RequestBody Ordination ordination) {
		
		ClinicAdmin admin;
		try {
			admin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can add new ordinations.");
		}
		if (admin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		ordination.setActive(true);
		Ordination o = ordinationService.update(admin.getClinic().getId(), ordination.getName(), ordination.getOrdinationNumber(), ordination, admin.getClinic());
			
		if(o == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ordination with given name and number combination already exists!");
		}
		
		return new ResponseEntity<>(new OrdinationDto(ordination), HttpStatus.CREATED);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/edit
	 HTTP request for editing ordination
	 parameter: String
	 returns ResponseEntity object
	 */
	@PostMapping(value = "/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void editOrdination(@RequestBody OrdinationDto editedOrdination) {
		
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admins can edit ordinations from their clinic.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Ordination ordination = ordinationService.findOneById(editedOrdination.getId());
		
		if((!ordination.getName().equals(editedOrdination.getName()) || 
		   !ordination.getOrdinationNumber().equals(editedOrdination.getOrdinationNumber())) &&
		   ordinationService.findOneByClinicIdAndNameAndOrdinationNumber(currentUser.getClinic().getId(),
		   editedOrdination.getName(), editedOrdination.getOrdinationNumber()) != null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ordination with given name and number already exists!");
		}
		
		ordinationService.update(ordination, editedOrdination);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/appointmentType/delete/{ordinationId}
	 HTTP request for deleting ordination
	 parameter: String
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/delete/{ordinationId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteAppType(@PathVariable Long ordinationId) {
		ClinicAdmin currentUser;
		try {
			currentUser = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic admins can remove ordinations from their clinic.");
		}
		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		Ordination ordination = ordinationService.findOneById(ordinationId);
		
		//provera da li ordinacija ima zakazane preglede
		//ukoliko ima zakazane preglede ne moze biti obrisana
		for(Appointment a : ordination.getAppointments()) {
			if(!a.getFinished()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ordination with scheduled appointments cant be deleted!");
			}
		}
		ordinationService.delete(ordination);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getAvailableExaminationRooms/{appointReqId}
	 HTTP request for getting all available examination rooms for given appointment request
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getAvailableExaminationRooms/{appReqId}", 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ExamRoomDto>> getAvailableExaminationRooms(@PathVariable("appReqId") Long id) throws ParseException {
		
		ClinicAdmin admin;
		try {
			admin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view available examination rooms.");
		}
		if (admin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		//uzimamo appointment request da bismo dosli do njegovog datuma
		AppointmentRequest appReq = appointmentRequestService.findOneById(id);
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		
		Date examDate = appReq.getAppointment().getDate();
		AppointmentType examType = appReq.getAppointment().getAppType();
		Doctor examDoctor = appReq.getAppointment().getDoctor();
		Integer examStartTime = appReq.getAppointment().getStartTime();
		Integer examEndTime = appReq.getAppointment().getEndTime();
		
		List<Integer> availableTimes = new ArrayList<Integer>();
		
		//sve ordinacije te klinike
		List<Ordination> examRooms = ordinationService.findAllByClinicIdAndType(admin.getClinic().getId(), OrdinationType.ConsultingRoom);
		
		// lista prihvatljivih ordinacija
		List<ExamRoomDto> satisfyingOrdinations = new ArrayList<ExamRoomDto>();
		
		// lista prihvatljivih ordinacija ZA DRUGI DATUM ili VREME
		List<ExamRoomDto> substituteOrdinations = new ArrayList<ExamRoomDto>();
		
		//prolazimo kroz ordinacije te klinike
		for (Ordination o : examRooms) {
			// uzimamo u obzir trenutnu ordinaciju
			boolean satisfying = true;
			// prolazimo kroz zakazane preglede u toj ordinaciji
			if (o.getAppointments() != null) {
				for (Appointment a : o.getAppointments()) {
					// proveravamo da li ima za taj datum zakazan pregled
					if (a.getDate().equals(examDate)) {
						// proveravamo od kada do kada traje zakazani pregled
						if (!(examEndTime <= a.getStartTime() || examStartTime >= a.getEndTime())) {
							satisfying = false;
							break;
						}
						
					}
				}
			}
			if(satisfying) {
				// dodajemo samo jedno vreme, ono za koji je zakazan
				// brisemo vremena prethodne
				availableTimes.clear();
				availableTimes.add(examStartTime);
				satisfyingOrdinations.add(new ExamRoomDto(o, dt.format(examDate), availableTimes));
			} else {
				// ne zadovoljava za trazeni datum i vreme
				// trazimo prvo slobodno vreme za koje ima slobodnu ordinaciju i da je doktor slobodan
				List<Integer> examRoomFree;
				Date newExamDate = examDate;
				while(true) {
					// idemo kroz datume dok ne nadjemo prvi za koji zadovoljavaju
					// kada je doktor slobodan
					List<Integer> doctorFree = examDoctor.getAvailableTimesForDateAndType(newExamDate, examType);
					examRoomFree = o.getAvailableTimesForDateAndType(newExamDate, examType);

					// presek slobodnih vremena
					examRoomFree.retainAll(doctorFree);
					
					if(!examRoomFree.isEmpty()) {
						// postoje slobodni i za doktora i za ordinaciju
						break;
					}
					// ne postoje slobodni za doktora i ordinaciju
					// uvecavamo dan za jedan
					newExamDate = new Date(examDate.getTime() + (1000 * 60 * 60 * 24));
				}
				substituteOrdinations.add(new ExamRoomDto(o, dt.format(newExamDate), examRoomFree));
			}
		}
		
		if(satisfyingOrdinations.isEmpty()) {
			// nema slobodnih soba za pregled
			// za svaku sobu vracamo datum za koji ima prvi slobodan termin
			// i listu termina
			return new ResponseEntity<>(substituteOrdinations, HttpStatus.OK);
		}
		
		// vracamo sve sobe za pregled
		return new ResponseEntity<>(satisfyingOrdinations, HttpStatus.OK);
	}
	
	/*
	 url: GET localhost:8081/theGoodShepherd/ordination/getAvailableOperationRooms/{appointReqId}
	 HTTP request for getting all available examination rooms for given appointment request
	 receives Ordination object
	 returns ResponseEntity object
	 */
	@GetMapping(value = "/getAvailableOperationRooms/{operReqId}", 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ExamRoomDto>> getAvailableOperationRooms(@PathVariable("operReqId") Long id) throws ParseException {
		
		ClinicAdmin admin;
		try {
			admin = (ClinicAdmin) session.getAttribute("currentUser");
		} catch (ClassCastException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clinic administrators can view available operation rooms.");
		}
		if (admin == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No user loged in!");
		}
		
		//uzimamo appointment request da bismo dosli do njegovog datuma
		AppointmentRequest appReq = appointmentRequestService.findOneById(id);
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		
		Date examDate = appReq.getAppointment().getDate();
		AppointmentType examType = appReq.getAppointment().getAppType();
		Doctor examDoctor = appReq.getAppointment().getDoctor();
		Integer examStartTime = appReq.getAppointment().getStartTime();
		Integer examEndTime = appReq.getAppointment().getEndTime();
		
		List<Integer> availableTimes = new ArrayList<Integer>();
		
		//sve ordinacije te klinike
		List<Ordination> examRooms = ordinationService.findAllByClinicIdAndType(admin.getClinic().getId(), OrdinationType.OperatingRoom);
		
		// lista prihvatljivih ordinacija
		List<ExamRoomDto> satisfyingOrdinations = new ArrayList<ExamRoomDto>();
		
		// lista prihvatljivih ordinacija ZA DRUGI DATUM ili VREME
		List<ExamRoomDto> substituteOrdinations = new ArrayList<ExamRoomDto>();
		
		//prolazimo kroz ordinacije te klinike
		for (Ordination o : examRooms) {
			// uzimamo u obzir trenutnu ordinaciju
			boolean satisfying = true;
			// prolazimo kroz zakazane preglede u toj ordinaciji
			if (o.getAppointments() != null) {
				for (Appointment a : o.getAppointments()) {
					// proveravamo da li ima za taj datum zakazan pregled
					if (a.getDate().equals(examDate)) {
						// proveravamo od kada do kada traje zakazani pregled
						if (!(examEndTime <= a.getStartTime() || examStartTime >= a.getEndTime())) {
							satisfying = false;
							break;
						}
						
					}
				}
			}
			if(satisfying) {
				// dodajemo samo jedno vreme, ono za koji je zakazan
				// brisemo vremena prethodne
				availableTimes.clear();
				availableTimes.add(examStartTime);
				satisfyingOrdinations.add(new ExamRoomDto(o, dt.format(examDate), availableTimes));
			} else {
				// ne zadovoljava za trazeni datum i vreme
				// trazimo prvo slobodno vreme za koje ima slobodnu ordinaciju i da je doktor slobodan
				List<Integer> examRoomFree;
				Date newExamDate = examDate;
				while(true) {
					// idemo kroz datume dok ne nadjemo prvi za koji zadovoljavaju
					// kada je doktor slobodan
					List<Integer> doctorFree = examDoctor.getAvailableTimesForDateAndType(newExamDate, examType);
					examRoomFree = o.getAvailableTimesForDateAndType(newExamDate, examType);

					// presek slobodnih vremena
					examRoomFree.retainAll(doctorFree);
					
					if(!examRoomFree.isEmpty()) {
						// postoje slobodni i za doktora i za ordinaciju
						break;
					}
					// ne postoje slobodni za doktora i ordinaciju
					// uvecavamo dan za jedan
					newExamDate = new Date(examDate.getTime() + (1000 * 60 * 60 * 24));
				}
				substituteOrdinations.add(new ExamRoomDto(o, dt.format(newExamDate), examRoomFree));
			}
		}
		
		if(satisfyingOrdinations.isEmpty()) {
			// nema slobodnih soba za pregled
			// za svaku sobu vracamo datum za koji ima prvi slobodan termin
			// i listu termina
			return new ResponseEntity<>(substituteOrdinations, HttpStatus.OK);
		}
		
		// vracamo sve sobe za pregled
		return new ResponseEntity<>(satisfyingOrdinations, HttpStatus.OK);
	}
}

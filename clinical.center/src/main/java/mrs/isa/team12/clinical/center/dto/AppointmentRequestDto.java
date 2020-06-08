package mrs.isa.team12.clinical.center.dto;

import java.sql.Date;

import mrs.isa.team12.clinical.center.model.AppointmentRequest;

public class AppointmentRequestDto {
	
	private Long id;
	private String doctorFullName;
	private String patientFullName;
	private Date date;
	private Integer startTime;
	private Integer endTime;
	
	public AppointmentRequestDto(AppointmentRequest appointmentRequest) {
		this.id = appointmentRequest.getId();
		this.doctorFullName = appointmentRequest.getAppointment().getDoctor().getName() + " " + appointmentRequest.getAppointment().getDoctor().getSurname();
		this.patientFullName = appointmentRequest.getAppointment().getPatient().getName() + " " + appointmentRequest.getAppointment().getPatient().getSurname();
		this.date = appointmentRequest.getAppointment().getDate();
		this.startTime = appointmentRequest.getAppointment().getStartTime();
		this.endTime = appointmentRequest.getAppointment().getEndTime();
	}
	
	public String getDoctorFullName() {
		return doctorFullName;
	}
	public void setDoctorFullName(String doctorFullName) {
		this.doctorFullName = doctorFullName;
	}
	public String getPatientFullName() {
		return patientFullName;
	}
	public void setPatientFullName(String patientFullName) {
		this.patientFullName = patientFullName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getStartTime() {
		return startTime;
	}
	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}
	public Integer getEndTime() {
		return endTime;
	}
	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}

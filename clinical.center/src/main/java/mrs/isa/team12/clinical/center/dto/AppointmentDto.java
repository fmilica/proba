package mrs.isa.team12.clinical.center.dto;

import java.util.Date;

import mrs.isa.team12.clinical.center.model.Appointment;

//mora da bude confirmed=true i finished = false
public class AppointmentDto {
	
	private Long id;
	private String appType;
	private String doctor;
	private String patient;
	private String ordination;
	private Date date;
	private Integer startTime;
	private Integer endTime;
	private Double price;
	private Double discount;
	private Boolean finished;
	private String typeOf;
	
	public AppointmentDto() {}
	
	public AppointmentDto(Appointment app) {
		this.id = app.getId();
		this.appType = app.getAppType().getName();
		this.doctor = app.getDoctor().getName() + " " + app.getDoctor().getSurname();
		if (app.getPatient() != null) {
			this.patient = app.getPatient().getName() + " " + app.getPatient().getSurname();	
		}
		this.ordination = app.getOrdination().getName() + " " + app.getOrdination().getOrdinationNumber();
		this.date = app.getDate();
		this.startTime = app.getStartTime();
		this.endTime = app.getEndTime();
		this.price = app.getAppType().getPrice();
		this.discount = app.getDiscount();
		this.finished = app.getFinished();
		if(app.getType() == null) {
			this.typeOf = "appointment";
		}else {
			this.typeOf = "operation";
		}
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getPatient() {
		return patient;
	}
	public void setPatient(String patient) {
		this.patient = patient;
	}
	public String getOrdination() {
		return ordination;
	}
	public void setOrdination(String ordination) {
		this.ordination = ordination;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public String getTypeOf() {
		return typeOf;
	}

	public void setTypeOf(String typeOf) {
		this.typeOf = typeOf;
	}
}

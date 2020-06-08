package mrs.isa.team12.clinical.center.dto;

public class AppointmentFollowupDto {

	private String date;
	private Integer time;
	private Long appTypeId;
	private String patientSecurityNumber;
	private Boolean operation;
	
	public AppointmentFollowupDto() {}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Long getAppTypeId() {
		return appTypeId;
	}
	public void setAppTypeId(Long appTypeId) {
		this.appTypeId = appTypeId;
	}
	public String getPatientSecurityNumber() {
		return patientSecurityNumber;
	}
	public void setPatientSecurityNumber(String patientSecurityNumber) {
		this.patientSecurityNumber = patientSecurityNumber;
	}
	public Boolean getOperation() {
		return operation;
	}
	public void setOperation(Boolean operation) {
		this.operation = operation;
	}
}

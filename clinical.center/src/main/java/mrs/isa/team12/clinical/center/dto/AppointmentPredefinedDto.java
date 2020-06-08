package mrs.isa.team12.clinical.center.dto;

public class AppointmentPredefinedDto {

	private String date;
	private String appTypeName;
	private Long doctorId;
	private Integer time;
	private Double discount;
	private Long ordinationId;
	private Long clinicId;

	public AppointmentPredefinedDto() {}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAppTypeName() {
		return appTypeName;
	}
	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Long getOrdinationId() {
		return ordinationId;
	}
	public void setOrdinationId(Long ordinationId) {
		this.ordinationId = ordinationId;
	}
	public Long getClinicId() {
		return clinicId;
	}
	public void setClinicId(Long clinicId) {
		this.clinicId = clinicId;
	}
}
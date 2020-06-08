package mrs.isa.team12.clinical.center.dto;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointmentTypesDto {

	private Long id;
	private List<AppointmentTypeDto> appTypes;
	
	public DoctorAppointmentTypesDto() {}
	
	public DoctorAppointmentTypesDto(Long doctorId) {
		this.id = doctorId;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<AppointmentTypeDto> getAppTypes() {
		return appTypes;
	}
	public void setAppTypes(List<AppointmentTypeDto> appTypes) {
		this.appTypes = appTypes;
	}
	
	public void addAppType(AppointmentTypeDto appType) {
		if (this.appTypes == null) {
			this.appTypes = new ArrayList<AppointmentTypeDto>();
		}
		this.appTypes.add(appType);
	}
}

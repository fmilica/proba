package mrs.isa.team12.clinical.center.dto;

import java.util.HashSet;
import java.util.Set;

import mrs.isa.team12.clinical.center.model.AppointmentType;
import mrs.isa.team12.clinical.center.model.Clinic;

public class ClinicPatientDto {
	
	private Long id;
	private String name;
	private String address;
	private Double rating;
	private Set<AppointmentTypeDto> appointmentTypes;
	
	public ClinicPatientDto(Clinic clinic) {
		this.id = clinic.getId();
		this.name = clinic.getName();
		this.address = clinic.getAddress();
		this.rating = clinic.getRating();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Set<AppointmentTypeDto> getAppointmentTypes() {
		return appointmentTypes;
	}
	public void setAppointmentTypes(Set<AppointmentType> appointmentTypes) {
		this.appointmentTypes = new HashSet<AppointmentTypeDto>();
		for(AppointmentType a : appointmentTypes) {
			this.appointmentTypes.add(new AppointmentTypeDto(a));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ClinicPatientDto)) {
			return false;
		}
		ClinicPatientDto c = (ClinicPatientDto) obj;
		return this.getId().equals(c.getId());
	}
}

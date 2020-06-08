package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.RegistrationRequest;

public class RegistrationRequestDto {
	private Long id;
	private PatientDto user;
	private Boolean approved;
	private String description;
	
	public RegistrationRequestDto() {}
	
	public RegistrationRequestDto(RegistrationRequest rr) {
		this.id = rr.getId();
		this.user = new PatientDto(rr.getUser());
		this.approved = rr.getApproved();
		this.description = rr.getDescription();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PatientDto getUser() {
		return user;
	}

	public void setUser(PatientDto user) {
		this.user = user;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.Prescription;

public class PrescriptionDto {
	private Long id;
	private String name;
	
	public PrescriptionDto() {}
	
	public PrescriptionDto(Prescription p) {
		this.id = p.getId();
		this.name = p.getMedicine();
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
}

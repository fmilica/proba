package mrs.isa.team12.clinical.center.dto;

import mrs.isa.team12.clinical.center.model.DiagnosePerscription;

public class DiagnosisPrescriptionDto {
	private Long id;
	
	public DiagnosisPrescriptionDto() {}

	public DiagnosisPrescriptionDto(Long id) {
		super();
		this.id = id;
	}
	
	public DiagnosisPrescriptionDto(DiagnosePerscription dp) {
		super();
		this.id = dp.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

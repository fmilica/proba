package mrs.isa.team12.clinical.center.dto;

import java.util.ArrayList;
import java.util.Collection;

import mrs.isa.team12.clinical.center.model.Diagnosis;
import mrs.isa.team12.clinical.center.model.Prescription;

public class DiagnosisDto {
	private Long id;
	private String name;
	private DiagnosisPrescriptionDto dpDto;
	private Collection<PrescriptionDto> prescriptions;
	
	public DiagnosisDto() {}
	
	public DiagnosisDto(Diagnosis d) {
		this.id = d.getId();
		this.name = d.getName();
		this.dpDto = new DiagnosisPrescriptionDto(d.getDiagnosePerscription());
		this.prescriptions = new ArrayList<PrescriptionDto>();
		if(d.getPrescriptions() != null) {
			for (Prescription p : d.getPrescriptions()) {
				prescriptions.add(new PrescriptionDto(p));
			}
		}
		
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

	public DiagnosisPrescriptionDto getDpDto() {
		return dpDto;
	}

	public void setDpDto(DiagnosisPrescriptionDto dpDto) {
		this.dpDto = dpDto;
	}

	public Collection<PrescriptionDto> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Collection<PrescriptionDto> prescriptions) {
		this.prescriptions = prescriptions;
	}
	
}

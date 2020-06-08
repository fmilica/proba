package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "prescription")
@Where(clause="is_active=true")
public class Prescription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "medicine", unique = false, nullable = false)
	private String medicine;
	
	@ManyToMany(mappedBy = "prescriptions")
	private Set<MedicalReport> medicalReports;
	
	//CascadeType.PERSIST, CascadeType.MERGE
	@ManyToMany(cascade={ALL})
	@JoinTable(name = "perscription_diagnosis", 
		joinColumns = @JoinColumn(name = "perscription_id"),
		inverseJoinColumns = @JoinColumn(name = "diagnosis_id"))
	private Set<Diagnosis> diagnosis;
	
	public Prescription() {}

	public Prescription(String medicine) {
		super();
		this.medicine = medicine;
	}
	
	public Prescription(Long id, String medicine, Set<MedicalReport> medicalReports, Set<Diagnosis> diagnosis) {
		super();
		this.id = id;
		this.medicine = medicine;
		this.medicalReports = medicalReports;
		this.diagnosis = diagnosis;
		this.active = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
	
	public Set<Diagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Set<Diagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void addDiagnosis(Diagnosis diagnosis) {
		this.diagnosis.add(diagnosis);
	}
	
	public void addMedicalReport(MedicalReport medicalReport) {
		if (this.medicalReports == null) {
			this.medicalReports = new HashSet<MedicalReport>();
		}
		if (!this.medicalReports.contains(medicalReport)) {
			this.medicalReports.add(medicalReport);
		}
	}
}

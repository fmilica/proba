package mrs.isa.team12.clinical.center.model;

import static javax.persistence.FetchType.LAZY;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import mrs.isa.team12.clinical.center.model.enums.LeaveType;

@Entity
@Table(name = "leave")
@Where(clause="is_active=true")
public class Leave {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "start_date", nullable = false, unique = false)
	//@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "end_date", nullable = false, unique = false)
	//@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Column(name = "leave_type", nullable = false, unique = false)
	private LeaveType type;
	
	@ManyToOne
	@JoinColumn(name= "medical_personnel_id", referencedColumnName = "id", nullable = false)
	private MedicalPersonnel medicalPersone;
	
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "leave_request_id")
	private LeaveRequest leaveRequest;

	public Leave() {}
	
	public Leave(Date startDate, Date endDate, LeaveType type, MedicalPersonnel mp) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.medicalPersone = mp;
		this.active = true;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public LeaveType getType() {
		return type;
	}

	public void setType(LeaveType type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public MedicalPersonnel getMedicalPersone() {
		return medicalPersone;
	}

	public void setMedicalPersone(MedicalPersonnel medicalPersone) {
		this.medicalPersone = medicalPersone;
	}
	
}

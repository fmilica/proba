package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "leave_request")
@Where(clause="is_active=true")
public class LeaveRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@OneToOne(cascade = {ALL}, fetch = EAGER)
	@JoinColumn(name = "leave_id")
	private Leave leave;
	
	@Column(name = "approved", nullable = false, unique = false)
	private Boolean approved;
	
	@Column(name = "description", nullable = true, unique = false)
	private String description;
	
	public LeaveRequest() {}

	public LeaveRequest(Leave leave, Boolean approved, String description) {
		super();
		this.leave = leave;
		this.approved = approved;
		this.description = description;
		this.active = true;
	}

	public Leave getLeave() {
		return leave;
	}

	public void setLeave(Leave leave) {
		this.leave = leave;
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
}

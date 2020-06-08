package mrs.isa.team12.clinical.center.model;

import static javax.persistence.FetchType.LAZY;

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
@Table(name = "registration_request")
@Where(clause="is_active=true")
public class RegistrationRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "registered_user_id")
	private RegisteredUser user;
	
	@Column(name = "approved", unique = false, nullable = false)
	private Boolean approved;
	
	@Column(name = "description", unique = false, nullable = true)
	private String description;
	
	public RegistrationRequest() {}
	
	public RegistrationRequest(RegisteredUser user, Boolean approved, String description) {
		super();
		this.user = user;
		this.approved = approved;
		this.description = description;
		this.active = true;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RegisteredUser getUser() {
		return user;
	}

	public void setUser(RegisteredUser user) {
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}

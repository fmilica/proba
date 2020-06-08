package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

import mrs.isa.team12.clinical.center.model.enums.OrdinationType;

@Entity
@Table(name = "ordination")
@Where(clause="is_active=true")
public class Ordination {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name = "ordination_type", unique = false, nullable = false)
	private OrdinationType type;
	
	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@Column(name = "ordination_number", unique = false, nullable = false)
	private Integer ordinationNumber;
	
	@ManyToOne
	@JoinColumn(name = "clinic_id", referencedColumnName = "id", nullable = false)
	private Clinic clinic;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "ordination")
	private Set<Appointment> appointments;

	public Ordination() {}
	
	public Ordination(OrdinationType type, String name, Integer ordinationNumber, Clinic clinic, Set<Appointment> appointments) {
		super();
		this.type = type;
		this.name = name;
		this.ordinationNumber = ordinationNumber;
		this.clinic = clinic;
		this.appointments = appointments;
		this.active = true;
	}

	public OrdinationType getType() {
		return type;
	}
	public void setType(OrdinationType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Clinic getClinic() {
		return clinic;
	}
	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}
	public Set<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrdinationNumber() {
		return ordinationNumber;
	}
	public void setOrdinationNumber(Integer ordinationNumber) {
		this.ordinationNumber = ordinationNumber;
	}
	
	public void addAppointment(Appointment a) {
		this.appointments.add(a);
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Ordination [id=" + id + ", type=" + type + ", name=" + name + ", clinic=" + clinic + ", appointments="
				+ appointments + "]";
	}
	
	// dobavljanje slobodnih termina ordinacije za odredjeni datum
	public List<Integer> getAvailableTimesForDateAndType(Date date, AppointmentType type) {
		// slobodna vremena za taj dan i tu ordinaciju
		List<Integer> times = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			times.add(i);
		}
		if (this.getAppointments() != null) {
			for (Appointment a : this.getAppointments()) {
				// samo gledamo potvrdjene preglede
				// ipak ne, gledamo sve
				//if (a.getConfirmed()) {
					if (a.getDate().equals(date)) {
						Integer start = a.getStartTime();
						for (int i = 0; i < a.getAppType().getDuration(); i++) {
							times.remove(Integer.valueOf(start+i));
						}
					}
				//}
			}
		}
		// imamo listu slobodnih vremena
		// provera da li imamo dovoljno uzastopnih sati za pregled
		List<Integer> freeTimes = new ArrayList<Integer>();
		for(Integer i : times) {
			boolean hasConsecutive = true;
			for(int j = 1; j < type.getDuration(); j++) {
				if (!times.contains(i+j)) {
					hasConsecutive = false;
				}
			}
			if (hasConsecutive) {
				freeTimes.add(i);
			}
		}
		return times;
	}
}

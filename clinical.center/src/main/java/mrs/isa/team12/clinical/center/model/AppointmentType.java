package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "appointment_type")
@Where(clause="is_active=true")
public class AppointmentType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Version
	private Long version;
	
	@Column(name="is_active")
	private Boolean active;
	
	// ime ne mora biti jedinstveno jer razlicite klinike mogu imati isti naziv za tip pregleda
	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@Column(name = "duration", unique = false, nullable = false )
	private Integer duration;
	
	@Column(name = "price", unique = false, nullable = false)
	private Double price;
	
	@ManyToOne
	@JoinColumn(name = "clinic_id", referencedColumnName = "id", nullable = false)
	private Clinic clinic;

	/*@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = true)*/
	@ManyToMany(cascade = {ALL})
    @JoinTable(
        name = "appointment_type_doctor", 
        joinColumns = { @JoinColumn(name = "appointment_type_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "doctor_id") }
    )
	private Set<Doctor> doctors;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "appType")
	private Set<Appointment> appointments;

	/*many to many veza jer eto znamo i sami*/
	/*@ManyToMany(cascade = {
    CascadeType.PERSIST,
    CascadeType.MERGE
	})
	@JoinTable(name = "post_tag",
	    joinColumns = @JoinColumn(name = "post_id"),
	    inverseJoinColumns = @JoinColumn(name = "tag_id")
	)*/
	
	public AppointmentType() {}
	
	public AppointmentType(String name, Integer duration, Double price) {
		this.name = name;
		this.duration = duration;
		this.price = price;
		this.active = true;
	}

	public AppointmentType(Long id, String name, Integer duration, Set<Appointment> appointments, Set<Doctor> doctors) {
		super();
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.appointments = appointments;
		this.doctors = doctors;
		this.active = true;
	}

	public AppointmentType(Long id, String name, Set<Appointment> appointments, Set<Doctor> doctors) {
		super();
		this.id = id;
		this.name = name;
		this.appointments = appointments;
		this.doctors = doctors;
		this.active = true;
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

	public Set<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Set<Doctor> getDoctors() {
		return doctors;
	}

	public void setDoctors(Set<Doctor> doctors) {
		this.doctors = doctors;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "AppointmentType [name=" + name + ", duration=" + duration + ", price=" + price + "]";
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o)
	        return true;
	    if (o == null)
	        return false;
	    if (this.getClass() != o.getClass())
	        return false;
	    AppointmentType appType = (AppointmentType) o;
	    return this.name.equals(appType.getName());
	}
	
	public void addDoctor(Doctor d) {
		if(!this.doctors.contains(d)) {
			this.doctors.add(d);
		}
	}
	
	public void removeDoctor(Doctor d) {
		if(this.doctors.contains(d)) {
			this.doctors.remove(d);
		}
	}
	
	public void addAppointment(Appointment a) {
		if(!this.appointments.contains(a)) {
			this.appointments.add(a);
		}
	}
	
	@Override
	public int hashCode() {
	      return name.hashCode();
	}
	
	
}

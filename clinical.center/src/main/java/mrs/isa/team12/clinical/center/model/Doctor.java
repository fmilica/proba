package mrs.isa.team12.clinical.center.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mrs.isa.team12.clinical.center.model.enums.Specialization;

@Entity
@Table(name="doctor")
public class Doctor extends MedicalPersonnel {
	
	@Column(name = "specialization", unique = false, nullable = false)
	private Specialization specialization;
	
	// u rasponu od 0 do 24
	@Column(name = "start_work", unique = false, nullable = false)
	private Integer startWork;
	
	@Column(name = "end_work", unique = false, nullable = false)
	private Integer endWork;
	
	@Column(name = "rating", unique = false, nullable = true)
	private Double rating;
	
	//@OneToMany(cascade = {ALL}, fetch = LAZY)
	//dodato za probu kaskadnog brisanja
	@ManyToMany(mappedBy = "doctors",  cascade = CascadeType.ALL)
	private Set<AppointmentType> appointmentTypes;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "doctor")
	private Set<Appointment> appointments;
	
	//dodato zbog operacija	
	@ManyToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "doctors")	
	private Set<Appointment> operationAppointments;
	
	@OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "doctor")
	private Set<Rating> ratings;
	

	public Doctor() {}

	public Doctor(Long id) {
		this.setId(id);
	}

	public Doctor(Integer startWork, Integer endWork, Double rating, Clinic clinic, Set<AppointmentType> appointmentTypes,
			Set<Appointment> appointments, Set<Rating> ratings) {
		super();
		this.startWork = startWork;
		this.endWork = endWork;
		this.rating = rating;
		this.appointmentTypes = appointmentTypes;
		this.appointments = appointments;
		this.ratings = ratings;
	}


	public Integer getStartWork() {
		return startWork;
	}
	public void setStartWork(Integer startWork) {
		this.startWork = startWork;
	}
	public Integer getEndWork() {
		return endWork;
	}
	public void setEndWork(Integer endWork) {
		this.endWork = endWork;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Set<AppointmentType> getAppointmentTypes() {
		return appointmentTypes;
	}
	public void setAppointmentTypes(Set<AppointmentType> appointmentTypes) {
		// uklanjamo doktora iz svih tipova pregleda koje vise NEMA
		for(AppointmentType at : this.appointmentTypes) {
			at.removeDoctor(this);
		}
		this.appointmentTypes = appointmentTypes;
		// setuje i tipovima njega za doktora
		for(AppointmentType at : appointmentTypes) {
			at.addDoctor(this);
		}
	}
	public Set<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}
	public Set<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	public Specialization getSpecialization() {
		return specialization;
	}
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public void addAppointment(Appointment appointment) {
		this.appointments.add(appointment);
	}

	@Override
	public String toString() {
		return super.toString() + "Doctor [specialization=" + specialization + ", startWork=" + startWork + ", endWork=" + endWork
				+ ", rating=" + rating + ", appointmentTypes=" + appointmentTypes
				+ ", appointments=" + appointments + ", ratings=" + ratings + "]";
	}

	// dobavljanje slobodnih termina doktora za odredjeni datum
	public List<Integer> getAvailableTimesForDateAndType(Date date, AppointmentType type) {
		// slobodna vremena za taj dan i tog doktora
		List<Integer> times = new ArrayList<Integer>();
								// da bi se pregled zavrsio za vreme radnog vremena doktora
		for (int i = this.getStartWork(); i <= this.getEndWork() - type.getDuration(); i++) {
			times.add(i);
		}
		if (this.getAppointments() != null) {
			for (Appointment a : this.getAppointments()) {
				// gledamo samo potvrdjene preglede
				// ipak ne, gledamo sve!
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

	public Set<Appointment> getOperationAppointments() {
		return operationAppointments;
	}

	public void setOperationAppointments(Set<Appointment> operationAppointments) {
		this.operationAppointments = operationAppointments;
	}
}

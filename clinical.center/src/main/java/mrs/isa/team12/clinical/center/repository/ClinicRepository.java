package mrs.isa.team12.clinical.center.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mrs.isa.team12.clinical.center.model.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
	
	Clinic findOneById(Long id);
	
	Clinic findOneByName(String name);
	
	@Query("SELECT c FROM Clinic c, AppointmentType a WHERE c.id = a.clinic AND a.id = ?1")
	List<Clinic> findAllByAppointmentTypeId(Long appTypeId);
	
	// ovo radi, ovo je da samo da klinika ima taj appType
	//@Query("SELECT c FROM Clinic c LEFT JOIN AppointmentType a ON c.id = a.clinic WHERE a.name = ?1")
	// sad treba da pokusam da klinika ima appType I DOKTORA KOJI IMA TU MOGUCNOST
	// joj, ne mogu, mislim da treba dupli join da se radi, to jest i dodatni join sa tabelom doktori-appTypovi
	// jer je ta veza ManyToMany
	@Query("SELECT c FROM Clinic c LEFT JOIN AppointmentType a ON c.id = a.clinic WHERE a.name = ?1")
	List<Clinic> findAllByAppointmentTypeName(String appTypeName);
	
	/*@Query("SELECT c "
			+ "FROM Clinic c "
			+ "LEFT JOIN "
			+ "SELECT AppointmentType a LEFT JOIN AppointmentTypeDoctor ad ON a.id = ad.appointmentTypeId "
			+ "WHERE a.name = ?1 "
			+ "ON c.id = a.clinic")
	sqlText = 
    "select entityA 
     from EntityA entityA, EntityB entityB 
     where entityA.fieldA=entityB.fieldA 
     and entityA.fieldC=(select sum(entityB.fieldB) 
                         from EntityB entityB 
                         where entityB.fieldA=entityA.fieldA)
    @Query("SELECT c "
    		+ "FROM Clinic c "
    		+ "WHERE c.id = a.clinic "
    		+ "AND  ")*/
	/*@Query("SELECT d "
			+ "FROM Doctor d, AppointmentTypeDoctor ad, AppointmentType a "
			+ "WHERE d.id = ad.doctorId AND ad.appointmentTypeId = a.id AND a.name = ?1")
	List<Clinic> findAllBySertifiedDoctor(String appTypeName);*/
}

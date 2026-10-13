package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // B4
    @Query("SELECT DISTINCT p FROM Patient p JOIN p.appointments a WHERE a.doctor.id = :doctorId")
    List<Patient> findPatientsOfDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT p FROM Patient p JOIN p.appointments a " +
            "GROUP BY p HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);
}

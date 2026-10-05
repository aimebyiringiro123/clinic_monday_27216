package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsBySsNumber(String ssNumber);

    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> findDistinctPatientsOfDoctor(@Param("doctorId") UUID doctorId);

    @Query("SELECT p FROM Appointment a JOIN a.patient p GROUP BY p HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);
}

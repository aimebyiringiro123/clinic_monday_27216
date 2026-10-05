package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import kigali.clinick.com.clinickmanagentsystem.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    boolean existsByFirstNameAndLastNameAndDateOfBirthAndOffice(
            String firstName, String lastName, Date dateOfBirth, Office office);

    Optional<Doctor> findByOffice(Office office);

    List<Doctor> findByFirstNameContainingIgnoreCase(String search);

    @Query("SELECT DISTINCT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findBySpecializationName(@Param("name") String name);

    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName")
    List<Doctor> findDoctorsWithoutOffice();
}

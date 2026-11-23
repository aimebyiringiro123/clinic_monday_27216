package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.model.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    boolean existsByOffice_OfficeNumberAndIdNot(String officeNumber, Long id);

    // B1
    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findDoctorsBySpecialization(@Param("name") String name);

    // B2
    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName")
    List<Doctor> findDoctorsWithoutOffice();

    @Query("SELECT d FROM Doctor d " +
            "WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "ORDER BY d.lastName")
    List<Doctor> searchByName(@Param("name") String name);


    @Query("SELECT d.firstName, d.lastName, COUNT(a) FROM Appointment a " +
            "JOIN a.doctor d " +
            "WHERE a.status = :status " +
            "AND a.appointmentDate BETWEEN :start AND :end " +
            "GROUP BY d.id, d.firstName, d.lastName " +
            "ORDER BY COUNT(a) DESC")
    List<Object[]> findWorkload(@Param("status") AppointmentStatus status,
                                @Param("start") LocalDate start,
                                @Param("end") LocalDate end);
}

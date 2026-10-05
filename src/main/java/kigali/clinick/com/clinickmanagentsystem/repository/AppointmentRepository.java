package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.Appointment.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

    boolean existsByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDate appointmentDate, AppointmentStatus status);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countGroupedByStatus();

    @Modifying
    @Query("UPDATE Appointment a SET a.status = :cancelled " +
            "WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> :completed")
    int cancelDayExceptCompleted(@Param("doctorId") UUID doctorId,
                                 @Param("date") LocalDate date,
                                 @Param("cancelled") AppointmentStatus cancelled,
                                 @Param("completed") AppointmentStatus completed);

    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = :status AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("status") AppointmentStatus status, @Param("date") LocalDate date);
}

package kigali.clinick.com.clinickmanagentsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "appointment")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"id", "dateOfBirth", "appointments", "ssNumber"})
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnoreProperties({"id", "dateOfBirth", "office", "appointments", "specializations"})
    private Doctor doctor;

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED
    }
}

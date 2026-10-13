package kigali.clinick.com.clinickmanagentsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prescription")
@Getter @Setter @NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String medicineName;
    private String dosage;
    private int durationDays;
    private LocalDate issuedDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}

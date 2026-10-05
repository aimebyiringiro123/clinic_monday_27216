package kigali.clinick.com.clinickmanagentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Table (name="patient")
@Data
public class Patient {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (name="first_name")
    private String firstName;

    @Column (name="last_name")
    private String lastName;

    @Column (name = "social_security_number", unique = true)
    private String ssNumber;

    @Column (name="date_of_birth")
    private Date dateOfBirth;

    @OneToMany (mappedBy = "patient")
    @JsonIgnore
    private List<Appointment> appointments = new ArrayList<>();

}

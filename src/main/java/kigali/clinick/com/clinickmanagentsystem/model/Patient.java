package kigali.clinick.com.clinickmanagentsystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name="first_name")
    private String firstName;

    @Column (name="last_name")
    private String lastName;

    @JsonIgnore
    @OneToMany (mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();
}

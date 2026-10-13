package kigali.clinick.com.clinickmanagentsystem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import java.util.List;


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

    @Column(unique = true)
    private String ssNumber;

    @JsonIgnore
    @OneToMany (mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();
}

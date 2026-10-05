package kigali.clinick.com.clinickmanagentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Table (name="office")
@Data
public class Office {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;


    @Column (name="name")
    private String name;

    @Column (name="office_number")
    private int officeNumber;

    @Column (name="location")
    private String location;

    @OneToOne (mappedBy = "office")
    @JsonIgnore
    private Doctor doctor;


}

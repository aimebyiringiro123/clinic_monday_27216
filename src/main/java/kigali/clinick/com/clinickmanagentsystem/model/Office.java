package kigali.clinick.com.clinickmanagentsystem.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Office {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;


    @Column (name="name")
    private String name;

    @Column (name="office_number")
    private int officeNumber;

    @Column (name="location")
    private String location;

    @JsonIgnore
    @OneToOne (mappedBy = "office")
    private Doctor doctor;



}

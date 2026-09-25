package kigali.clinick.com.clinickmanagentsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "office")
@Data
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "office_number")
    private int officeNumber;
    private String name;
    private String location;

}

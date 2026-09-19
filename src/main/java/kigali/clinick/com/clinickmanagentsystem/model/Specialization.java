package kigali.clinick.com.clinickmanagentsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specializations")
@Data
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "specialization")
    private List<DoctorSpecialization> doctorSpecializations = new ArrayList<>();

}

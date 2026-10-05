package kigali.clinick.com.clinickmanagentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "specialization")
@Data
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "specializations")
    @JsonIgnore
    private List<Doctor> doctors = new ArrayList<>();
}

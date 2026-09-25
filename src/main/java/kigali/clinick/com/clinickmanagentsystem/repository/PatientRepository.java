package kigali.clinick.com.clinickmanagentsystem.repository;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByPhone(String phone);
    Patient findById(long id);

    List<Patient> getByPhone(String phone);
}

package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import kigali.clinick.com.clinickmanagentsystem.repository.DoctorRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.OfficeRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.SpecializationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;

    public DoctorService(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }


    public Doctor create(Doctor doctor) {
        doctor.setId(null);
        return doctorRepo.save(doctor);
    }


    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }


    public Doctor findById(Long id) {
        return doctorRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "doctor not found"));
    }
    public Doctor update(Long id, Doctor data) {
        Doctor existing = findById(id);     // 404 if missing
        existing.setFirstName(data.getFirstName());
        existing.setLastName(data.getLastName());
        existing.setOffice(data.getOffice());
        return doctorRepo.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        doctorRepo.deleteById(id);
    }

    public List<Doctor> findByLastName(String lastName) {
        return doctorRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Doctor> findBySpecialization(String name) {
        return doctorRepo.findDoctorsBySpecialization(name);   // calls the repository method
    }

    public List<Doctor> findWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }

}
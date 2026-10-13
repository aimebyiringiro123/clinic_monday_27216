package kigali.clinick.com.clinickmanagentsystem.services;

import jakarta.transaction.Transactional;
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
    private final SpecializationRepository specializationRepo;
    private final OfficeRepository officeRepo;

    public DoctorService(DoctorRepository doctorRepo, SpecializationRepository specializationRepo, OfficeRepository officeRepo) {
        this.doctorRepo = doctorRepo;
        this.specializationRepo = specializationRepo;
        this.officeRepo = officeRepo;
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

    @Transactional
    public String addSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Specialization> specOpt = specializationRepo.findById(specializationId);
        if (specOpt.isEmpty()) {
            return "That specialization does not exist";
        }

        Doctor doctor = doctorOpt.get();
        Specialization spec = specOpt.get();

        if (doctor.getSpecializations().contains(spec)) {
            return "The doctor already has that specialization";
        }

        doctor.getSpecializations().add(spec);
        doctorRepo.save(doctor);
        return "Specialization added to the doctor";
    }


    @Transactional
    public String removeSpecialization(Long doctorId, Long specializationId) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Specialization> specOpt = specializationRepo.findById(specializationId);
        if (specOpt.isEmpty()) {
            return "That specialization does not exist";
        }

        Doctor doctor = doctorOpt.get();
        Specialization spec = specOpt.get();

        if (!doctor.getSpecializations().contains(spec)) {
            return "The doctor does not have that specialization";
        }

        doctor.getSpecializations().remove(spec);
        doctorRepo.save(doctor);
        return "Specialization removed from the doctor";
    }


    public String changeOffice(Long doctorId, String officeNumber) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Office> officeOpt = officeRepo.findByOfficeNumber(officeNumber);
        if (officeOpt.isEmpty()) {
            return "The office number does not exist";
        }

        if (doctorRepo.existsByOffice_OfficeNumberAndIdNot(officeNumber, doctorId)) {
            return "That office is taken by other doctor";
        }

        Doctor doctor = doctorOpt.get();
        doctor.setOffice(officeOpt.get());     // Doctor owns the @JoinColumn
        doctorRepo.save(doctor);
        return "Office changed successfully";
    }
}
package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import kigali.clinick.com.clinickmanagentsystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    public Patient savePatient(Patient patient) {
        if (patientRepo.existsBySsNumber(patient.getSsNumber())) {
            throw new IllegalStateException("ssNUmber already exists");
        }
        return patientRepo.save(patient);
    }

    public void deletePatient(UUID id) {
        if (!patientRepo.existsById(id)) {
            throw new IllegalStateException("patient with id: " + id + " does not exist");
        }
        patientRepo.deleteById(id);
    }

    public List<Patient> getAllPatient() {
        return patientRepo.findAll();
    }

    public List<Patient> findByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> findPatientsOfDoctor(UUID doctorId) {
        return patientRepo.findDistinctPatientsOfDoctor(doctorId);
    }

    public List<Patient> findFrequentPatients(long min) {
        return patientRepo.findFrequentPatients(min);
    }

    public String updatePatient(UUID id, Patient updatedPatient) {
        Patient existing = patientRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Patient with id: " + id + " does not exist"));

        existing.setFirstName(updatedPatient.getFirstName());
        existing.setLastName(updatedPatient.getLastName());
        existing.setDateOfBirth(updatedPatient.getDateOfBirth());

        patientRepo.save(existing);

        return "Patient updated successfully!";
    }

    public Patient getPatientById(UUID id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Patient with id " + id + " does not exist"));
    }
}

package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import kigali.clinick.com.clinickmanagentsystem.repository.AppointmentRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.DoctorRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class PatientService {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;

    public PatientService(PatientRepository patientRepo, DoctorRepository doctorRepo, AppointmentRepository appointmentRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }


    public Patient create(Patient patient) {
        patient.setId(null);
        return patientRepo.save(patient);
    }

    public List<Patient> findAll() {
        return patientRepo.findAll();
    }

    public Patient findById(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Patient not found"));
    }
    public Patient update(Long id, Patient data) {
        Patient existing = findById(id);
        existing.setFirstName(data.getFirstName());
        existing.setLastName(data.getLastName());
        return patientRepo.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        patientRepo.deleteById(id);
    }

    // A1
    public List<Patient> findByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> findPatientsOfDoctor(Long doctorId) {
        if (!doctorRepo.existsById(doctorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The doctor with that id does not exist");
        }
        return patientRepo.findPatientsOfDoctor(doctorId);
    }

    public List<Patient> findFrequent(long min) {
        return patientRepo.findFrequentPatients(min);
    }

    public Optional<List<Appointment>> upcoming(String ssNumber) {
        if (!patientRepo.existsBySsNumber(ssNumber)) {
            return Optional.empty();
        }
        return Optional.of(appointmentRepo
                .findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                        ssNumber,
                        LocalDate.now(),
                        List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED)));
    }
}

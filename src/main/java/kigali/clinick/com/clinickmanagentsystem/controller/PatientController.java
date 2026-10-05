package kigali.clinick.com.clinickmanagentsystem.controller;

import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import kigali.clinick.com.clinickmanagentsystem.services.DoctorService;
import kigali.clinick.com.clinickmanagentsystem.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/patient", "/api/patients"})
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePatient(@RequestBody Patient patient) {
        try {
            return new ResponseEntity<>(patientService.savePatient(patient), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/by-last-name")
    public List<Patient> byLastName(@RequestParam String lastName) {
        return patientService.findByLastName(lastName);
    }

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> ofDoctor(@PathVariable UUID doctorId) {
        if (!doctorService.existsById(doctorId)) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(patientService.findPatientsOfDoctor(doctorId));
    }

    @GetMapping("/frequent")
    public List<Patient> frequent(@RequestParam long min) {
        return patientService.findFrequentPatients(min);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {
        try {
            patientService.deletePatient(id);
            return new ResponseEntity<>("patient deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/get")
    public List<Patient> getPatient() {
        return patientService.getAllPatient();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        try {
            String message = patientService.updatePatient(id, patient);

            if (message.equalsIgnoreCase("patient updated successfully!")) {
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
            }
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

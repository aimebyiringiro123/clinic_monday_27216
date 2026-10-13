package kigali.clinick.com.clinickmanagentsystem.controller;

import kigali.clinick.com.clinickmanagentsystem.model.Patient;

import kigali.clinick.com.clinickmanagentsystem.services.PatientService;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@RequestBody Patient patient) {
        return patientService.create(patient);
    }

    @GetMapping(value = "/getAll")
    public List<Patient> findAll() {
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    public Patient findById(@PathVariable Long id) {
        return patientService.findById(id);
    }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.update(id, patient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }

    // A1
    @GetMapping("/by-last-name")
    public List<Patient> byLastName(@RequestParam String lastName) {
        return patientService.findByLastName(lastName);
    }

    @GetMapping("/of-doctor/{doctorId}")
    public List<Patient> ofDoctor(@PathVariable Long doctorId) {
        return patientService.findPatientsOfDoctor(doctorId);
    }

    @GetMapping("/frequent")
    public List<Patient> frequent(@RequestParam long min) {
        return patientService.findFrequent(min);
    }
}

package kigali.clinick.com.clinickmanagentsystem.controller;

import jakarta.validation.Valid;
import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import kigali.clinick.com.clinickmanagentsystem.services.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor create(@RequestBody Doctor doctor) {
        return doctorService.create(doctor);
    }

    @GetMapping(value = "/getAll")
    public List<Doctor> findAll() {
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    public Doctor findById(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.update(id, doctor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        doctorService.delete(id);
    }

    // A1
    @GetMapping("/by-last-name")
    public List<Doctor> byLastName(@RequestParam String lastName) {
        return doctorService.findByLastName(lastName);
    }

    @GetMapping("/by-specialization")
    public List<Doctor> bySpecialization(@RequestParam String name) {
        return doctorService.findBySpecialization(name);
    }

    @GetMapping("/without-office")
    public List<Doctor> withoutOffice() {
        return doctorService.findWithoutOffice();
    }
}

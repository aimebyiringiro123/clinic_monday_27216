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

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            Doctor saved = doctorService.saveDoctor(doctor);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> assignSpecialization(@PathVariable UUID doctorId,
                                                 @PathVariable UUID specializationId) {
        try {
            return ResponseEntity.ok(doctorService.assignSpecialization(doctorId, specializationId));
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-specialization")
    public List<Doctor> bySpecialization(@RequestParam String name) {
        return doctorService.findBySpecializationName(name);
    }

    @GetMapping("/without-office")
    public List<Doctor> withoutOffice() {
        return doctorService.findWithoutOffice();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {
        try {
            doctorService.deleteDoctor(id);
            return new ResponseEntity<>("doctor deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/get")
    public List<Doctor> getAll() {
        return doctorService.getAllDoctor();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
        try {
            String message = doctorService.updateDoctor(id, doctor);

            if (message.equalsIgnoreCase("doctor updated successfully!")) {
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
            }
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

package kigali.clinick.com.clinickmanagentsystem.controller;


import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import kigali.clinick.com.clinickmanagentsystem.services.DoctorService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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



    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> addSpecialization(@PathVariable Long doctorId,
                                                    @PathVariable Long specializationId) {
        String message = doctorService.addSpecialization(doctorId, specializationId);

        if (message.equals("The doctor with that id does not exist")
                || message.equals("That specialization does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("The doctor already has that specialization")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }


    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<String> removeSpecialization(@PathVariable Long doctorId,
                                                       @PathVariable Long specializationId) {
        String message = doctorService.removeSpecialization(doctorId, specializationId);

        if (message.equals("The doctor with that id does not exist")
                || message.equals("That specialization does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("The doctor does not have that specialization")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<String> changeOffice(@PathVariable Long doctorId,
                                               @RequestParam String officeNumber) {
        String message = doctorService.changeOffice(doctorId, officeNumber);

        if (message.equals("The doctor with that id does not exist")
                || message.equals("The office number does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("That office is taken by other doctor")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }
}

package kigali.clinick.com.clinickmanagentsystem.controller;

import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import kigali.clinick.com.clinickmanagentsystem.services.SpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Specialization specialization) {
        try {
            return new ResponseEntity<>(specializationService.saveSpecialization(specialization), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/unused")
    public List<Specialization> unused() {
        return specializationService.findUnused();
    }

    @GetMapping("/get")
    public List<Specialization> getAll() {
        return specializationService.getAllSpecialization();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Specialization specialization) {
        try {
            return ResponseEntity.ok(specializationService.updateSpecialization(id, specialization));
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            specializationService.deleteSpecialization(id);
            return new ResponseEntity<>("Specialization deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

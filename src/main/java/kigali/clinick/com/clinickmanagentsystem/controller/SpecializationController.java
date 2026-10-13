package kigali.clinick.com.clinickmanagentsystem.controller;

import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import kigali.clinick.com.clinickmanagentsystem.services.SpecializationService;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping(value = "save")
    @ResponseStatus(HttpStatus.CREATED)
    public Specialization create(@RequestBody Specialization specialization) {
        return specializationService.create(specialization);
    }

    @GetMapping(value = "getAll")
    public List<Specialization> findAll() {
        return specializationService.findAll();
    }

    @GetMapping("/{id}")
    public Specialization findById(@PathVariable Long id) {
        return specializationService.findById(id);
    }

    @PutMapping("/{id}")
    public Specialization update(@PathVariable Long id, @RequestBody Specialization specialization) {
        return specializationService.update(id, specialization);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        specializationService.delete(id);
    }

    @GetMapping("/unused")
    public List<Specialization> unused() {
        return specializationService.findUnused();
    }
}

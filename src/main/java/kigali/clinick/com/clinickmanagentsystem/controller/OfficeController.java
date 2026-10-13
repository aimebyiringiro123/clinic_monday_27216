package kigali.clinick.com.clinickmanagentsystem.controller;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.services.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/offices")
public class OfficeController {
    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }


    @PostMapping(value = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Office create(@RequestBody Office office) {
        return officeService.create(office);
    }

    @GetMapping(value = "/getAll")
    public List<Office> findAll() {
        return officeService.findAll();
    }

    @GetMapping("/{id}")
    public Office findById(@PathVariable Long id) {
        return officeService.findById(id);
    }

    @PutMapping("/{id}")
    public Office update(@PathVariable Long id, @RequestBody Office office) {
        return officeService.update(id, office);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        officeService.delete(id);
    }

    // A1
    @GetMapping("/by-name")
    public List<Office> byOfficeNumber(@RequestParam String name) {
        return officeService.findByOfficeName(name);
    }

    @GetMapping("/free")
    public List<Office> free() {
        return officeService.findFree();
    }
}

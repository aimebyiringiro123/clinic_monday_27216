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
@RequestMapping(value = {"/api/office", "/api/offices"})
public class OfficeController {

    private final OfficeService officeServes;

    public OfficeController(OfficeService officeServes) {
        this.officeServes = officeServes;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {
        String returnedMessage = officeServes.saveOffice(office);

        if (returnedMessage.equalsIgnoreCase("Office is saved Successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }
/*
    @GetMapping("/busiest")
    public Object busiest() {
        return officeServes.getBusiestOffice();
    }

 */

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> d(@PathVariable UUID id) {
        try {
            officeServes.deleteOffice(id);
            return new ResponseEntity<>("Office deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/get")
    public List<Office> getOffice() {
        return officeServes.getAllOffice();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
        try {
            String message = officeServes.updateOffice(id, office);

            if (message.equalsIgnoreCase("office updated successfully!")) {
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
            }
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

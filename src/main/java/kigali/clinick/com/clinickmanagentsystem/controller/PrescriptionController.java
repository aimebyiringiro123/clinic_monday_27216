package kigali.clinick.com.clinickmanagentsystem.controller;
import kigali.clinick.com.clinickmanagentsystem.model.Prescription;
import kigali.clinick.com.clinickmanagentsystem.services.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> create(@RequestParam Long appointmentId,
                                         @RequestBody Prescription prescription) {
        String message = prescriptionService.create(appointmentId, prescription);

        if (message.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("Prescriptions are only for COMPLETED appointments")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> byAppointment(@PathVariable Long appointmentId) {
        Optional<List<Prescription>> result = prescriptionService.findByAppointment(appointmentId);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The appointment with that id does not exist");
        }
        return ResponseEntity.ok(result.get());
    }

    @GetMapping("/patient/{ssNumber}")
    public List<Prescription> history(@PathVariable String ssNumber) {
        return prescriptionService.findHistory(ssNumber);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id,
                                         @RequestBody Prescription prescription) {
        String message = prescriptionService.update(id, prescription);

        if (message.equals("The prescription with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = prescriptionService.delete(id);

        if (message.equals("The prescription with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/stats/top-medicines")
    public List<Object[]> topMedicines() {
        return prescriptionService.topMedicines();
    }
}
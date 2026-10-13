package kigali.clinick.com.clinickmanagentsystem.controller;


import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.services.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/appointments")

public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> create(@RequestBody Appointment appointment) {
        String message = appointmentService.create(appointment);

        if (message.equals("Appointment date cannot be in the past")
                || message.equals("Appointment date is required")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }


    @GetMapping(value = "/getAll")
    public List<Appointment> findAll() {
        return appointmentService.findAll();
    }
    @GetMapping("/{id}")
    public Appointment findById(@PathVariable Long id) {
        return appointmentService.findById(id);
    }
    @GetMapping("/by-status")
    public List<Appointment> byStatus(@RequestParam AppointmentStatus status) {
        return appointmentService.findByStatus(status);
    }


    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.update(id, appointment);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appointmentService.delete(id);
    }


    @GetMapping("/between")
    public List<Appointment> between(@RequestParam String start, @RequestParam String end) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            return appointmentService.findBetween(startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dates must use the format yyyy-MM-dd");
        }
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> statsByStatus() {
        return appointmentService.countByStatus();
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Long id,
                                               @RequestParam AppointmentStatus status) {
        String message = appointmentService.changeStatus(id, status);

        if (message.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.startsWith("Cannot change status")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.ok(message);
    }



    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> reschedule(@PathVariable Long id,
                                             @RequestParam LocalDate newDate) {
        String message = appointmentService.reschedule(id, newDate);

        if (message.equals("The appointment with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.endsWith("cannot be rescheduled")
                || message.equals("Appointment date cannot be in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (message.equals("Doctor is already booked on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }
}

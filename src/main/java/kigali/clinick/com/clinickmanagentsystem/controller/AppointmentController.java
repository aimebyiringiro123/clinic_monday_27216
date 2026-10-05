package kigali.clinick.com.clinickmanagentsystem.controller;

import jakarta.validation.Valid;
import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.Appointment.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.services.AppointmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            Appointment saved = appointmentService.saveAppointment(appointment);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/by-status")
    public List<Appointment> getByStatus(@RequestParam AppointmentStatus status) {
        return appointmentService.findByStatus(status);
    }

    @GetMapping(value = "/between")
    public List<Appointment> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return appointmentService.findBetween(startDate, endDate);
    }

    @GetMapping(value = "/stats/by-status")
    public List<Object[]> statsByStatus() {
        return appointmentService.countByStatus();
    }

    @PatchMapping(value = "/cancel-day")
    public String cancelDay(@RequestParam UUID doctorId, @RequestParam String date) {
        return appointmentService.cancelDay(doctorId, LocalDate.parse(date));
    }

    @GetMapping(value = "/page")
    public Object getPage(@RequestParam int page, @RequestParam int size, @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort sortSpec = parts.length > 1
                ? Sort.by(Sort.Direction.fromString(parts[1]), parts[0])
                : Sort.by(parts[0]);
        return appointmentService.findPage(PageRequest.of(page, size, sortSpec));
    }

    @DeleteMapping(value = "/cancelled-before")
    public String deleteCancelledBefore(@RequestParam String date) {
        return appointmentService.deleteCancelledBefore(LocalDate.parse(date));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentById(id));
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/get")
    public List<Appointment> getAllAppointment() {
        return appointmentService.getAllAppointments();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment) {
        try {
            String message = appointmentService.updateAppointment(id, appointment);

            if (message.equalsIgnoreCase("appointment updated successfully!")) {
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
            }
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id) {
        try {
            appointmentService.deleteAppointment(id);
            return new ResponseEntity<>("Appointment deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

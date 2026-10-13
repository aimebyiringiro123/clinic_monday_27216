package kigali.clinick.com.clinickmanagentsystem.services;


import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.repository.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;

    public AppointmentService(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public String create(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            return "Appointment date is required";
        }
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }

        appointment.setId(null);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment saved successfully";
    }

    public List<Appointment> findAll() {
        return appointmentRepo.findAll();
    }

    public Appointment findById(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Appointment not found"));
    }
    public Appointment update(Long id, Appointment data) {
        Appointment existing = findById(id);
        existing.setAppointmentDate(data.getAppointmentDate());
        existing.setReason(data.getReason());
        existing.setPatient(data.getPatient());
        existing.setDoctor(data.getDoctor());
        return appointmentRepo.save(existing);
    }


    public void delete(Long id) {
        findById(id);
        appointmentRepo.deleteById(id);
    }

    //A2
    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    //A3
    public List<Appointment> findBetween(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> countByStatus() {
        return appointmentRepo.countByStatus();
    }



    public String changeStatus(Long id, AppointmentStatus newStatus) {
        Optional<Appointment> opt = appointmentRepo.findById(id);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = opt.get();
        AppointmentStatus current = appointment.getStatus();

        boolean allowed = false;
        if (current == AppointmentStatus.SCHEDULED) {
            allowed = newStatus == AppointmentStatus.CONFIRMED
                    || newStatus == AppointmentStatus.CANCELLED;
        } else if (current == AppointmentStatus.CONFIRMED) {
            allowed = newStatus == AppointmentStatus.COMPLETED
                    || newStatus == AppointmentStatus.CANCELLED;
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        appointment.setStatus(newStatus);
        appointmentRepo.save(appointment);
        return "Appointment status updated successfully";
    }




    public String reschedule(Long id, LocalDate newDate) {
        Optional<Appointment> opt = appointmentRepo.findById(id);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = opt.get();

        if (appointment.getStatus() == AppointmentStatus.COMPLETED
                || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return "A " + appointment.getStatus() + " appointment cannot be rescheduled";
        }

        if (newDate.isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }

        if (appointment.getDoctor() != null
                && appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(), newDate, AppointmentStatus.CANCELLED)) {
            return "Doctor is already booked on that date";
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

}

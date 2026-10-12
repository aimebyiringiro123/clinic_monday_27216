package kigali.clinick.com.clinickmanagentsystem.services;


import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.repository.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;

    public AppointmentService(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public Appointment create(Appointment appointment) {
        appointment.setId(null);

        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            boolean booked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
                    appointment.getDoctor().getId(),
                    appointment.getAppointmentDate(),
                    AppointmentStatus.CANCELLED);

            if (booked) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Doctor is already booked on that date");
            }
        }
        return appointmentRepo.save(appointment);
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
        Appointment existing = findById(id);     // 404 if missing
        existing.setAppointmentDate(data.getAppointmentDate());
        existing.setReason(data.getReason());
        existing.setStatus(data.getStatus());
        existing.setPatient(data.getPatient());
        existing.setDoctor(data.getDoctor());
        return appointmentRepo.save(existing);
    }


    public void delete(Long id) {
        findById(id);                    // throws 404 if it doesn't exist
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

}

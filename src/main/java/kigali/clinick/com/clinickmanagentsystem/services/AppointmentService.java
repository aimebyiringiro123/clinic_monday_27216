package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.Appointment.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import kigali.clinick.com.clinickmanagentsystem.repository.AppointmentRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.DoctorRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public Appointment saveAppointment(Appointment appointment) {
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            throw new IllegalStateException("Patient id is required");
        }
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            throw new IllegalStateException("Doctor id is required");
        }

        UUID patientId = appointment.getPatient().getId();
        UUID doctorId = appointment.getDoctor().getId();

        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new IllegalStateException("Patient with id: " + patientId + " does not exist"));

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException("Doctor with id: " + doctorId + " does not exist"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        if (appointmentRepo.existsByDoctorAndAppointmentDateAndStatusNot(
                doctor, appointment.getAppointmentDate(), AppointmentStatus.CANCELLED)) {
            throw new IllegalStateException("Doctor is already booked on that date");
        }

        return appointmentRepo.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Appointment getAppointmentById(UUID id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Appointment with id: " + id + " does not exist"));
    }

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> findBetween(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> countByStatus() {
        return appointmentRepo.countGroupedByStatus();
    }

    @Transactional
    public String cancelDay(UUID doctorId, LocalDate date) {
        int count = appointmentRepo.cancelDayExceptCompleted(
                doctorId, date, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);
        return count + " appointments cancelled";
    }

    public Page<Appointment> findPage(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        int count = appointmentRepo.deleteCancelledBefore(AppointmentStatus.CANCELLED, date);
        return count + " appointments deleted";
    }

    public String updateAppointment(UUID id, Appointment updatedAppointment) {
        Appointment existing = appointmentRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Appointment with id: " + id + " does not exist"));

        if (updatedAppointment.getAppointmentDate() != null) {
            existing.setAppointmentDate(updatedAppointment.getAppointmentDate());
        }
        if (updatedAppointment.getReason() != null) {
            existing.setReason(updatedAppointment.getReason());
        }
        if (updatedAppointment.getStatus() != null) {
            existing.setStatus(updatedAppointment.getStatus());
        }

        if (updatedAppointment.getPatient() != null && updatedAppointment.getPatient().getId() != null) {
            UUID patientId = updatedAppointment.getPatient().getId();
            Patient patient = patientRepo.findById(patientId)
                    .orElseThrow(() -> new IllegalStateException("Patient with id: " + patientId + " does not exist"));
            existing.setPatient(patient);
        }

        if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getId() != null) {
            UUID doctorId = updatedAppointment.getDoctor().getId();
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new IllegalStateException("Doctor with id: " + doctorId + " does not exist"));
            existing.setDoctor(doctor);
        }

        appointmentRepo.save(existing);
        return "appointment updated successfully!";
    }

    public void deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            throw new IllegalStateException("Appointment with id: " + id + " does not exist");
        }
        appointmentRepo.deleteById(id);
    }
}

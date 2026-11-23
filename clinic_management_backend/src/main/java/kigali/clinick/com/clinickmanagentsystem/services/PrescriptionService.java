package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Appointment;
import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.model.Prescription;
import kigali.clinick.com.clinickmanagentsystem.repository.AppointmentRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepo;
    private final AppointmentRepository appointmentRepo;

    public PrescriptionService(PrescriptionRepository prescriptionRepo,
                               AppointmentRepository appointmentRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.appointmentRepo = appointmentRepo;
    }

    public String create(Long appointmentId, Prescription prescription) {
        Optional<Appointment> opt = appointmentRepo.findById(appointmentId);
        if (opt.isEmpty()) {
            return "The appointment with that id does not exist";
        }

        Appointment appointment = opt.get();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return "Prescriptions are only for COMPLETED appointments";
        }

        prescription.setId(null);
        prescription.setIssuedDate(LocalDate.now());
        prescription.setAppointment(appointment);
        prescriptionRepo.save(prescription);
        return "Prescription saved successfully";
    }


    public Optional<List<Prescription>> findByAppointment(Long appointmentId) {
        if (!appointmentRepo.existsById(appointmentId)) {
            return Optional.empty();
        }
        return Optional.of(prescriptionRepo.findByAppointmentId(appointmentId));
    }

    public List<Prescription> findHistory(String ssNumber) {
        return prescriptionRepo.findHistoryBySsNumber(ssNumber);
    }


    public String update(UUID id, Prescription data) {
        Optional<Prescription> opt = prescriptionRepo.findById(id);
        if (opt.isEmpty()) {
            return "The prescription with that id does not exist";
        }

        Prescription existing = opt.get();
        existing.setMedicineName(data.getMedicineName());
        existing.setDosage(data.getDosage());
        existing.setDurationDays(data.getDurationDays());
        prescriptionRepo.save(existing);
        return "Prescription updated successfully";
    }

    public String delete(UUID id) {
        if (!prescriptionRepo.existsById(id)) {
            return "The prescription with that id does not exist";
        }
        prescriptionRepo.deleteById(id);
        return "Prescription deleted successfully";
    }

    public List<Object[]> topMedicines() {
        return prescriptionRepo.findTopMedicines();
    }
}
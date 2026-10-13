package kigali.clinick.com.clinickmanagentsystem.services;
import kigali.clinick.com.clinickmanagentsystem.model.AppointmentStatus;
import kigali.clinick.com.clinickmanagentsystem.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final OfficeRepository officeRepo;
    private final AppointmentRepository appointmentRepo;
    private final PrescriptionRepository prescriptionRepo;

    public DashboardService(PatientRepository patientRepo,
                            DoctorRepository doctorRepo,
                            OfficeRepository officeRepo,
                            AppointmentRepository appointmentRepo,
                            PrescriptionRepository prescriptionRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.officeRepo = officeRepo;
        this.appointmentRepo = appointmentRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    public Map<String, Long> summary() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("patients", patientRepo.count());
        result.put("doctors", doctorRepo.count());
        result.put("freeOffices", (long) officeRepo.findFreeOffices().size());
        result.put("appointmentsToday", appointmentRepo.countByAppointmentDate(LocalDate.now()));
        result.put("scheduled", appointmentRepo.countByStatus(AppointmentStatus.SCHEDULED));
        result.put("prescriptions", prescriptionRepo.count());
        return result;
    }
}
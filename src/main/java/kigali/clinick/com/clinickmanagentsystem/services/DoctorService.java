package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Doctor;
import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import kigali.clinick.com.clinickmanagentsystem.repository.DoctorRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.OfficeRepository;
import kigali.clinick.com.clinickmanagentsystem.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
    public class DoctorService {

        private final DoctorRepository doctorRepo;
        private final OfficeRepository officeRepository;
        private final SpecializationRepository specializationRepo;

        public DoctorService(DoctorRepository doctorRepo, OfficeRepository officeRepository, SpecializationRepository specializationRepo) {
            this.doctorRepo = doctorRepo;
            this.officeRepository = officeRepository;
            this.specializationRepo = specializationRepo;
        }

        public Doctor saveDoctor(Doctor doctor) {
            if (doctor.getOffice() != null) {
                int officeNumber = doctor.getOffice().getOfficeNumber();
                Optional<Office> officeOpt = officeRepository.findByOfficeNumber(officeNumber);

                if (officeOpt.isEmpty()) {
                    throw new IllegalStateException("Office with number " + officeNumber + " does not exist");
                }

                Office office = officeOpt.get();

                if (office.getDoctor() != null) {
                    throw new IllegalStateException("Office with number " + officeNumber + " is already occupied");
                }

                doctor.setOffice(office);
            }

            return doctorRepo.save(doctor);
        }

        public String assignSpecialization(UUID doctorId, UUID specializationId) {
            Doctor doctor = doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new IllegalStateException("doctor with id: " + doctorId + " does not exist"));
            Specialization specialization = specializationRepo.findById(specializationId)
                    .orElseThrow(() -> new IllegalStateException("specialization with id: " + specializationId + " does not exist"));

            doctor.getSpecializations().add(specialization);
            doctorRepo.save(doctor);
            return "specialization assigned to doctor";
        }

        public void deleteDoctor(UUID id) {
            if (!doctorRepo.existsById(id)) {
                throw new IllegalStateException("doctor with id: " + id + " does not exist");
            }
            doctorRepo.deleteById(id);
        }

        public List<Doctor> getAllDoctor() {
            return doctorRepo.findAll();
        }

        public boolean existsById(UUID id) {
            return doctorRepo.existsById(id);
        }

        public List<Doctor> findBySpecializationName(String name) {
            return doctorRepo.findBySpecializationName(name);
        }

        public List<Doctor> findWithoutOffice() {
            return doctorRepo.findDoctorsWithoutOffice();
        }

        public String updateDoctor(UUID id, Doctor updatedDoctor) {
            Doctor existing = doctorRepo.findById(id)
                    .orElseThrow(() -> new IllegalStateException("doctor with id: " + id + " does not exist"));

            existing.setFirstName(updatedDoctor.getFirstName());
            existing.setLastName(updatedDoctor.getLastName());
            existing.setDateOfBirth(updatedDoctor.getDateOfBirth());

            doctorRepo.save(existing);

            return "doctor updated successfully!";
        }
    }

package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import kigali.clinick.com.clinickmanagentsystem.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepo;

    public SpecializationService(SpecializationRepository specializationRepo) {
        this.specializationRepo = specializationRepo;
    }

    public Specialization create(Specialization specialization) {
        specialization.setId(null);                      // force a NEW row
        return specializationRepo.save(specialization);
    }

    public List<Specialization> findAll() {
        return specializationRepo.findAll();
    }


    public Specialization findById(Long id) {
        return specializationRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Specialization not found"));
    }
    public Specialization update(Long id, Specialization data) {
        Specialization existing = findById(id);          // 404 if missing
        existing.setName(data.getName());
        return specializationRepo.save(existing);
    }

    public void delete(Long id) {
        findById(id);                                    // 404 if missing
        specializationRepo.deleteById(id);
    }

    public List<Specialization> findUnused() {
        return specializationRepo.findUnused();
    }
}

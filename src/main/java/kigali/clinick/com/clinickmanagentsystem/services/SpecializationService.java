package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Specialization;
import kigali.clinick.com.clinickmanagentsystem.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    public Specialization saveSpecialization(Specialization specialization){
        specializationRepo.findByNameIgnoreCase(specialization.getName()).ifPresent(s -> {
            throw new IllegalStateException("Specialization '" + specialization.getName() + "' already exists");
        });
        return specializationRepo.save(specialization);
    }


    public String updateSpecialization(UUID id, Specialization specializationupdated) {
        Specialization existing = specializationRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Specialization with id " + id + " does not exist"));
        existing.setName(specializationupdated.getName());

        specializationRepo.save(existing);

        return "specialuzation saved successful";
    }


    public void deleteSpecialization(UUID id) {
        if (!specializationRepo.existsById(id)) {
            throw new IllegalStateException("Specialization with id " + id + " does not exist");
        }
        specializationRepo.deleteById(id);
    }


    public List<Specialization> getAllSpecialization(){
        return specializationRepo.findAll();
    }

    public List<Specialization> findUnused() {

        return specializationRepo.findUnused();
    }



}

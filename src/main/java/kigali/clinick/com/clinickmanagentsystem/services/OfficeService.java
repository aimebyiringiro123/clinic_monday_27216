package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.repository.OfficeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepo;

    public OfficeService(OfficeRepository officeRepo) {
        this.officeRepo = officeRepo;
    }


    public Office create(Office office) {
        office.setId(null);
        return officeRepo.save(office);
    }


    public List<Office> findAll() {
        return officeRepo.findAll();
    }


    public Office findById(Long id) {
        return officeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "office not found"));
    }
    public Office update(Long id, Office data) {
        Office existing = findById(id);
        existing.setName(data.getName());
        existing.setOfficeNumber(data.getOfficeNumber());
        existing.setLocation(data.getLocation());
        return officeRepo.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        officeRepo.deleteById(id);
    }

    public List<Office> findByOfficeName(String name) {
        return officeRepo.findByName(name);
    }


    public List<Office> findFree() {
        return officeRepo.findFreeOffices();
    }
}

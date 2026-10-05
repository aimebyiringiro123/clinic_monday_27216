package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepo;

    public String saveOffice(Office office) {
        Optional<Office> existing = officeRepo.findByOfficeNumber(office.getOfficeNumber());

        if (existing.isPresent()) {
            return "Office with that number: " + office.getOfficeNumber() + " already exists";
        }

        officeRepo.save(office);
        return "Office is saved Successfully";
    }

    public void deleteOffice(UUID id) {
        if (!officeRepo.existsById(id)) {
            throw new IllegalStateException("Office with id: " + id + " does not exist");
        }
        officeRepo.deleteById(id);
    }

    public List<Office> getAllOffice() {
        return officeRepo.findAll();
    }
/*
    public Object getBusiestOffice() {
        List<Object[]> rows = officeRepo.findBusiestOffice();
        if (rows.isEmpty()) {
            return "No appointments yet";
        }
        return rows.get(0);
    }

 */

    public String updateOffice(UUID id, Office updatedOffice) {
        Office existing = officeRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Office with id: " + id + " does not exist"));

        Optional<Office> sameNumberOpt = officeRepo.findByOfficeNumber(updatedOffice.getOfficeNumber());

        if (sameNumberOpt.isPresent() && !sameNumberOpt.get().getId().equals(id)) {
            return "Office with that number: " + updatedOffice.getOfficeNumber() + " already exists";
        }

        existing.setOfficeNumber(updatedOffice.getOfficeNumber());
        existing.setName(updatedOffice.getName());
        existing.setLocation(updatedOffice.getLocation());

        officeRepo.save(existing);
        return "office updated successfully!";
    }
}

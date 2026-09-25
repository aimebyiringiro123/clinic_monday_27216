package kigali.clinick.com.clinickmanagentsystem.services;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {


    @Autowired
    private OfficeRepository officeRepo;

//========== SAVE OR ADD OFFICE =============
    public String saveOffice(Office office){

       if(officeRepo.findByOfficeNumber(office.getOfficeNumber()) != null){
            return "Office with that number:"+office.getOfficeNumber() + " already exists";
        }


        officeRepo.save(office);

        return "Office is saved Successfully";
    }

//============== DELETE OR REMOVE OFFICE =================
public void deleteOffice(Long id){
    if(!officeRepo.existsById(id)){
        throw new IllegalStateException("Office with id: " + id + " does not exist");
    }
    officeRepo.deleteById(id);
}


//======================= READ OR GET all  =====================
    public List<Office> getAllOffice(){
        return officeRepo.findAll();
    }

}



package kigali.clinick.com.clinickmanagentsystem.services;

import jakarta.validation.constraints.Null;
import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import kigali.clinick.com.clinickmanagentsystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

//========= save or Add ==============
    public String savePatient(Patient patient){
       if(patientRepo.findByPhone(patient.getPhone()) != null){
            return "patient with phone: " + patient.getPhone() + " already exist";
        }

        patientRepo.save(patient);

        return "patient saved successful";
    }


//============== DELETE OR REMOVE Patient ================
    public void deletePatient(Long id){
        if(!patientRepo.existsById(id)){
            throw new IllegalStateException("patient with id: " + id + " does not exist");
        }
        patientRepo.deleteById(id);
    }


    //===================== READ or GET all=========================

    public List<Patient> getAllPatient(){

        return patientRepo.findAll();
    }
}





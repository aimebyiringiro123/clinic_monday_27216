package kigali.clinick.com.clinickmanagentsystem.controller;

import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.model.Patient;
import kigali.clinick.com.clinickmanagentsystem.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces  = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> savePatient(@RequestBody Patient patient){
        String returnedMessage = patientService.savePatient(patient);

        if (returnedMessage.equalsIgnoreCase("patient id saved ")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return new ResponseEntity<>("patient deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping
    public List<Patient> getPatient(){
        return patientService.getAllPatient();
    }


}

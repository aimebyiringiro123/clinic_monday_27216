package kigali.clinick.com.clinickmanagentsystem.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import kigali.clinick.com.clinickmanagentsystem.model.Office;
import kigali.clinick.com.clinickmanagentsystem.services.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/office")
public class OfficeController {

    @Autowired
    private OfficeService officeServes;



    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces  = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveOffice(@RequestBody Office office){

        String returnedMessage = officeServes.saveOffice(office);

      if(returnedMessage.equalsIgnoreCase("Office is saved Successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }
      else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }



    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable Long id) {
        try {
            officeServes.deleteOffice(id);
            return new ResponseEntity<>("Office deleted successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }





    @GetMapping
    public List<Office> getOffice(){
        return officeServes.getAllOffice();
    }



}

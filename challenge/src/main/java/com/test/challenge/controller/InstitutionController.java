package com.test.challenge.controller;

import com.test.challenge.dto.InstitutionDTO;
import com.test.challenge.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.test.challenge.service.InstitutionService;

@RestController
@RequestMapping(path = "/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @PostMapping()
    public ResponseEntity saveInstitution(@RequestBody InstitutionDTO institutionDTO)throws BadRequestException {
        if(institutionDTO != null) {
            institutionService.save(institutionDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(institutionService.find(id));
    }

    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity getAll() throws BadRequestException {
        return ResponseEntity.ok(institutionService.findAll());
    }

    @PutMapping()
    public ResponseEntity update(@RequestBody InstitutionDTO institutionDTO) throws BadRequestException {
        institutionService.update(institutionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws BadRequestException {
        institutionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/data")
    public ResponseEntity test(@PathVariable Long id){
        return ResponseEntity.ok(institutionService.getAllInstitutionData(id));
    }

}



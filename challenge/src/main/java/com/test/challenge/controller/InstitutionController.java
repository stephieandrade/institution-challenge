package com.test.challenge.controller;

import com.test.challenge.dto.InstitutionDTO;
import com.test.challenge.dto.InstitutionInfoDTO;
import com.test.challenge.service.InstitutionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @PostMapping()
    public ResponseEntity<InstitutionDTO> saveInstitution(@RequestBody InstitutionDTO institutionDTO){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        if(institutionDTO != null) {
            return new ResponseEntity<>(institutionService.save(institutionDTO), httpHeaders, HttpStatus.CREATED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionDTO> get(@PathVariable Long id){
        if(institutionService.find(id) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>(institutionService.find(id), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/all")
    public List<InstitutionDTO> getAll(){
        return institutionService.findAll();
    }

    @PutMapping()
    public InstitutionDTO update(@RequestBody InstitutionDTO institutionDTO) {
        return institutionService.update(institutionDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        institutionService.delete(id);
    }

    @GetMapping(path = "/{id}/data")
    public InstitutionInfoDTO getAllInstitutionData(@PathVariable Long id){
        return institutionService.getAllInstitutionData(id);
    }

}



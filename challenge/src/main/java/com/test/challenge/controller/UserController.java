package com.test.challenge.controller;

import com.test.challenge.dto.UserDTO;
import com.test.challenge.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity saveUser(@RequestBody UserDTO UserDTO)throws BadRequestException {
        return ResponseEntity.ok(userService.save(UserDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(userService.find(id));
    }

    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping()
    public ResponseEntity update(@RequestBody UserDTO UserDTO) throws BadRequestException {
        userService.update(UserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws BadRequestException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/institutions/{institutionId}")
    public ResponseEntity getUsersByInstitution(@PathVariable Long institutionId) throws BadRequestException {
        return ResponseEntity.ok(userService.findUsersByInstitution(institutionId));
    }

    @GetMapping("/primary-institution/{institutionId}")
    public ResponseEntity getUsersByPrimaryInstitution(@PathVariable Long institutionId) throws BadRequestException {
        return ResponseEntity.ok(userService.findUsersByPrimaryInstitution(institutionId));
    }

    @PostMapping("/{userId}/institutions/{institutionId}")
    public ResponseEntity<Void> assignUserToInstitution(
            @PathVariable Long userId,
            @PathVariable Long institutionId,
            @RequestParam(defaultValue = "false") boolean isPrimary) {
        userService.assignUserToInstitution(userId, institutionId, isPrimary);
        return ResponseEntity.ok().build();
    }
}

package com.test.challenge.controller;

import com.test.challenge.dto.UserDTO;
import com.test.challenge.exception.ApiCustomBadRequestException;
import com.test.challenge.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Object> saveUser(@RequestBody UserDTO UserDTO) {
        return ResponseEntity.accepted().body(UserDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(userService.find(id));
    }

    @GetMapping(path = "/all", produces = "application/json")
    public List<UserDTO> getAll(){
        return userService.findAll();
    }

    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody UserDTO UserDTO) {
        if (UserDTO == null){
            throw new ApiCustomBadRequestException("Error updating user");
        }else{
            return ResponseEntity.ok(UserDTO);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/institutions/{institutionId}")
    public List<UserDTO> getUsersByInstitution(@PathVariable Long institutionId) {
        return userService.findUsersByInstitution(institutionId);
    }

    @GetMapping("/primary-institution/{institutionId}")
    public List<UserDTO> getUsersByPrimaryInstitution(@PathVariable Long institutionId)  {
        return userService.findUsersByPrimaryInstitution(institutionId);
    }

    @PostMapping("/{userId}/institutions/{institutionId}")
    public void assignUserToInstitution(
            @PathVariable Long userId,
            @PathVariable Long institutionId,
            @RequestParam(defaultValue = "false") boolean isPrimary) {
        userService.assignUserToInstitution(userId, institutionId, isPrimary);
    }
}

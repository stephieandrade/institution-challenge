package com.test.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.challenge.dto.UserDTO;
import com.test.challenge.exception.ApiCustomBadRequestException;
import com.test.challenge.model.Institution;
import com.test.challenge.model.User;
import com.test.challenge.repository.InstitutionRepository;
import com.test.challenge.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IGenericService<UserDTO, Long>{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public UserDTO find(Long aLong)  {
        Optional<User> userFound = userRepository.findById(aLong);
        UserDTO userFoundDTO = new UserDTO();
        if (userFound.isPresent()) {
            userFoundDTO.setUserId(userFound.get().getUserId());
            userFoundDTO.setEmail(userFound.get().getEmail());
            userFoundDTO.setFirstName(userFound.get().getFirstName());
            userFoundDTO.setLastName(userFound.get().getLastName());
            userFoundDTO.setInstitutionId(userFound.get().getInstitution().getInstitutionId());
            return userFoundDTO;
        } else {
            throw new EntityNotFoundException("No user exists with id: " + aLong);
        }
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        Optional<Institution> foundInstitution = institutionRepository.findById(userDTO.getInstitutionId());
        if(userDTO.getEmail() == null){
            throw new ApiCustomBadRequestException("Email is missing in user data.");
        }
        if(foundInstitution.isPresent()){
            User user = new User();
            user.setInstitution(foundInstitution.get());
            user.setLastName(userDTO.getLastName());
            user.setFirstName(userDTO.getFirstName());
            user.setEmail(userDTO.getEmail());
            userRepository.save(user);}
        else{
            throw new EntityNotFoundException("Institution doesn't exist, therefore user can't be created for the institution established.");
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        if(!userList.isEmpty()){
            userList.forEach(dto -> userDTOList.add(new UserDTO(dto.getUserId(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getInstitution().getInstitutionId())));
        }
        return userDTOList;
    }

    public List<UserDTO> findUsersByInstitution(Long id) {
        if(id == null){
            throw new EntityNotFoundException("Institution does not exist.");
        }
        Optional<List<User>> userList = userRepository.findUsersByInstitution(id);

        List<UserDTO> userDTOList = new ArrayList<>();
        userList.ifPresent(users -> {
            users.forEach(dto -> userDTOList.add(new UserDTO(dto.getUserId(), dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getInstitution().getInstitutionId())));
        });

        return userDTOList;
    }

    public List<UserDTO> findUsersByPrimaryInstitution(Long institutionId) {
        Optional<List<User>> userList = userRepository.findUsersByPrimaryInstitution(institutionId);
        if(userList.isPresent()){
                return userList.get().stream().map(user -> new UserDTO(user.getUserId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getInstitution().getInstitutionId())).collect(Collectors.toList());
        }else{
            throw new EntityNotFoundException("Users not found for the Institution id selected");
        }
    }



    @Override
    public UserDTO update(UserDTO userDTO) throws BadRequestException {
        Optional<User> foundUser = userRepository.findById(userDTO.getUserId());
        Optional<Institution> foundInstitution = institutionRepository.findById(userDTO.getInstitutionId());
        if(foundUser.isPresent() && foundInstitution.isPresent()){
            foundUser.get().setFirstName(userDTO.getFirstName());
            foundUser.get().setLastName(userDTO.getLastName());
            foundUser.get().setEmail(userDTO.getEmail());
            foundUser.get().setInstitution(foundInstitution.get());
            return mapper.convertValue(userRepository.save(foundUser.get()), UserDTO.class);
        }else{
            throw new BadRequestException("User could not be updated.");
        }
    }

    @Override
    public void delete(Long aLong){
        Optional<User> foundUser = userRepository.findById(aLong);
        if(foundUser.isPresent()){
            userRepository.deleteById(aLong);
        }else{
            throw new EntityNotFoundException("User could not be deleted as it does not exist in the database.");
        }
    }

    public void assignUserToInstitution(Long userId, Long institutionId, boolean isPrimary) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new EntityNotFoundException("Institution not found with id: " + institutionId));
        user.getInstitutions().add(institution);
        if (isPrimary) {
            user.setInstitution(institution);
        }
        userRepository.save(user);
    }
}

package com.test.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.challenge.dto.InstitutionDTO;
import com.test.challenge.dto.InstitutionInfoDTO;
import com.test.challenge.model.Institution;
import com.test.challenge.model.User;
import com.test.challenge.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.test.challenge.repository.InstitutionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService implements IGenericService<InstitutionDTO, Long> {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public InstitutionDTO find(Long aLong) throws BadRequestException {
        Optional<Institution> institutionFound = institutionRepository.findById(aLong);
        if(institutionFound.isPresent()){
            InstitutionDTO institutionDTO = new InstitutionDTO();
            Optional<List<User>> users = userRepository.findUsersByInstitution(institutionFound.get().getInstitutionId());
            users.ifPresent(institutionDTO::setUsers);
            institutionDTO.setName(institutionFound.get().getName());
            institutionDTO.setInstitutionId(institutionFound.get().getInstitutionId());
            return institutionDTO;
        } else{
            throw new BadRequestException("Data is null");
        }
    }

    @Override
    public InstitutionDTO save(InstitutionDTO institutionDTO) throws BadRequestException {
        if(institutionDTO == null){
            throw new BadRequestException("Data is null");
        }else{
            Institution institution = mapper.convertValue(institutionDTO, Institution.class);
            institutionRepository.save(institution);
        }
        return institutionDTO;
    }

    @Override
    public List<InstitutionDTO> findAll() throws BadRequestException {
        List<Institution> institutionList = institutionRepository.findAll();
        List<InstitutionDTO> institutionDTOList = new ArrayList<>();
        if(!institutionList.isEmpty()){
            institutionList.forEach(institution -> {
                InstitutionDTO institutionDTO = new InstitutionDTO();
                Optional<List<User>> users = userRepository.findUsersByInstitution(institution.getInstitutionId());
                users.ifPresent(institutionDTO::setUsers);
                institutionDTO.setName(institution.getName());
                institutionDTO.setInstitutionId(institution.getInstitutionId());
                institutionDTOList.add(institutionDTO);
            });
        } else{
            throw new BadRequestException("Data is null");
        }
        return institutionDTOList;
    }

    @Override
    public InstitutionDTO update(InstitutionDTO institutionDTO) throws BadRequestException {
        Optional<Institution> foundInstitution = institutionRepository.findById(institutionDTO.getInstitutionId());
        if(foundInstitution.isPresent()){
            foundInstitution.get().setName(institutionDTO.getName());
            foundInstitution.get().setUsers(institutionDTO.getUsers());
            return mapper.convertValue(institutionRepository.save(foundInstitution.get()), InstitutionDTO.class);
        }else{
            throw new BadRequestException("Institution could not be updated as it does not exist in the database.");
        }
    }

    @Override
    public void delete(Long aLong) throws BadRequestException {
        Optional<Institution> foundInstitution = institutionRepository.findById(aLong);
        if(foundInstitution.isPresent()){
            institutionRepository.deleteById(aLong);
        }else{
            throw new BadRequestException("Institution could not be deleted as it does not exist in the database.");
        }
    }

    public InstitutionInfoDTO getAllInstitutionData(Long id){
        Optional<List<Object[]>> rawResult = institutionRepository.getData(id);
        if (rawResult.isPresent()) {
            Object[] result = rawResult.get().get(0); // Assuming the query returns one row for the given ID
            Long institutionId = ((Number) result[0]).longValue();
            String name = (String) result[1];
            Long userCount = ((Number) result[2]).longValue();
            return new InstitutionInfoDTO(institutionId, name, userCount);
        }else{
            throw new RuntimeException("Institution not found with id: " + id);
        }
    }
}

package com.test.challenge.service.institution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.challenge.dto.InstitutionDTO;
import com.test.challenge.dto.InstitutionInfoDTO;
import com.test.challenge.dto.UserDTO;
import com.test.challenge.model.Institution;
import com.test.challenge.model.User;
import com.test.challenge.repository.InstitutionRepository;
import com.test.challenge.repository.UserRepository;
import com.test.challenge.service.InstitutionService;
import com.test.challenge.service.UserService;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private InstitutionService institutionService;

    @InjectMocks
    private UserService userService;

    @Test
    void save() throws BadRequestException {
        Institution mockedInstitution = new Institution();
        mockedInstitution.setInstitutionId(1L);
        mockedInstitution.setName("Mocked institution");

        HashSet<Institution> mockedInstitutions = new HashSet<>();
        mockedInstitutions.add(mockedInstitution);

        List<User> mockedUserList = new ArrayList<>();
        mockedUserList.add(new User(1L, "email1", "firstname1", "lastname1", mockedInstitution, mockedInstitutions));

        mockedInstitution.setUsers(mockedUserList);

        InstitutionDTO institutionDTO = new InstitutionDTO(1L, "Mocked institution", mockedUserList);

        when(institutionRepository.save(Mockito.any(Institution.class))).thenReturn(mockedInstitution);
        when(mapper.convertValue(institutionDTO, Institution.class)).thenReturn(mockedInstitution);


        InstitutionDTO savedInstitution = institutionService.save(institutionDTO);

        assertNotNull(savedInstitution);
        assertEquals(mockedInstitution.getInstitutionId(), savedInstitution.getInstitutionId());
        assertEquals(mockedInstitution.getName(), savedInstitution.getName());
    }

    @Test
    void find() throws BadRequestException{
        Long institutionId = 1L;

        Institution mockedInstitution = new Institution();
        mockedInstitution.setInstitutionId(institutionId);
        mockedInstitution.setName("Mocked institution");

        HashSet<Institution> mockedInstitutions = new HashSet<>();
        mockedInstitutions.add(mockedInstitution);

        List<User> mockedUserList = new ArrayList<>();
        mockedUserList.add(new User(institutionId, "email1", "firstname1", "lastname1", mockedInstitution, mockedInstitutions));

        mockedInstitution.setUsers(mockedUserList);

        InstitutionDTO institutionDTO = new InstitutionDTO(institutionId, "Mocked institution", mockedUserList);

        when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(mockedInstitution));

        InstitutionDTO result = institutionService.find(institutionId);

        assertNotNull(result);
        assertEquals(institutionDTO.getInstitutionId(), result.getInstitutionId());
        assertEquals(institutionDTO.getName(), result.getName());

        verify(institutionRepository, times(1)).findById(institutionId);
    }

    @Test
    void findAll() throws BadRequestException {
        Institution institution1 = new Institution();
        institution1.setInstitutionId(1L);
        institution1.setName("Institution 1");

        Institution institution2 = new Institution();
        institution2.setInstitutionId(2L);
        institution2.setName("Institution 2");

        List<Institution> mockInstitutionList = Arrays.asList(institution1, institution2);

        InstitutionDTO institutionDTO1 = new InstitutionDTO();
        institutionDTO1.setInstitutionId(1L);
        institutionDTO1.setName("Institution 1");

        InstitutionDTO institutionDTO2 = new InstitutionDTO();
        institutionDTO2.setInstitutionId(2L);
        institutionDTO2.setName("Institution 2");

        User mockedUser = new User();
        mockedUser.setUserId(1L);
        mockedUser.setFirstName("name");
        mockedUser.setLastName("last name");
        mockedUser.setEmail("email");
        mockedUser.setInstitution(institution1);

        User mockedUser2 = new User();
        mockedUser2.setUserId(2L);
        mockedUser2.setFirstName("name2");
        mockedUser2.setLastName("last name2");
        mockedUser2.setEmail("email2");
        mockedUser2.setInstitution(institution2);

        List<User> mockedUsers = Arrays.asList(mockedUser, mockedUser2);

        when(institutionRepository.findAll()).thenReturn(mockInstitutionList);
        when(userRepository.findUsersByInstitution(Mockito.any(Long.class))).thenReturn(Optional.of(mockedUsers));

        List<InstitutionDTO> result = institutionService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Institution 1", result.get(0).getName());
        assertEquals("Institution 2", result.get(1).getName());

        verify(institutionRepository, times(1)).findAll();
    }

    @Test
    void update() throws BadRequestException{
        Long institutionId = 1L;

        Institution mockedInstitution = new Institution();
        mockedInstitution.setInstitutionId(institutionId);
        mockedInstitution.setName("Mocked institution");

        HashSet<Institution> mockedInstitutions = new HashSet<>();
        mockedInstitutions.add(mockedInstitution);

        List<User> mockedUserList = new ArrayList<>();
        mockedUserList.add(new User(institutionId, "email1", "firstname1", "lastname1", mockedInstitution, mockedInstitutions));

        InstitutionDTO institutionDTO = new InstitutionDTO();
        institutionDTO.setInstitutionId(1L);
        institutionDTO.setName("Institution 1");
        institutionDTO.setUsers(mockedUserList);

        mockedInstitution.setUsers(mockedUserList);

        when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(mockedInstitution));
        when(institutionRepository.save(Mockito.any(Institution.class))).thenReturn(mockedInstitution);
        when(mapper.convertValue(mockedInstitution, InstitutionDTO.class)).thenReturn(institutionDTO);

        institutionDTO.setName("Institution updated");
        InstitutionDTO updatedInstitution = institutionService.update(institutionDTO);

        Assertions.assertThat(updatedInstitution).isNotNull();
        assertEquals("Institution updated", updatedInstitution.getName());
    }

    @Test
    void delete() {
        Long institutionId = 1L;

        Institution mockedInstitution = new Institution();
        mockedInstitution.setInstitutionId(institutionId);
        mockedInstitution.setName("Mocked institution");

        HashSet<Institution> mockedInstitutions = new HashSet<>();
        mockedInstitutions.add(mockedInstitution);

        List<User> mockedUserList = new ArrayList<>();
        mockedUserList.add(new User(institutionId, "email1", "firstname1", "lastname1", mockedInstitution, mockedInstitutions));

        mockedInstitution.setUsers(mockedUserList);

        when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(mockedInstitution));

        assertAll(()-> institutionService.delete(institutionId));
    }

    @Test
    void getAllInstitutionData() {
        Long id = 1L;
        Long institutionId = 1L;
        String name = "Test Institution";
        Long userCount = 42L;

        List<Object[]> rawData = new ArrayList<>();
        rawData.add(new Object[]{institutionId, name, userCount});

        when(institutionRepository.getData(id)).thenReturn(Optional.of(rawData));

        InstitutionInfoDTO result = institutionService.getAllInstitutionData(id);

        assertNotNull(result);
        assertEquals(institutionId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(userCount, result.getUserCount());

        verify(institutionRepository, times(1)).getData(id);
    }

    @Test
    void findAllUsersByInstitutionId() throws BadRequestException {
        Long institutionId = 1L;

        Institution institution = new Institution();
        institution.setInstitutionId(institutionId);
        institution.setName("Institution 1");

        User mockedUser = new User();
        mockedUser.setUserId(1L);
        mockedUser.setFirstName("name");
        mockedUser.setLastName("last name");
        mockedUser.setEmail("email");
        mockedUser.setInstitution(institution);

        User mockedUser2 = new User();
        mockedUser2.setUserId(2L);
        mockedUser2.setFirstName("name2");
        mockedUser2.setLastName("last name2");
        mockedUser2.setEmail("email2");
        mockedUser2.setInstitution(institution);

        List<User> mockedUsers = Arrays.asList(mockedUser, mockedUser2);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setFirstName("name");
        userDTO.setLastName("last name");
        userDTO.setEmail("email");
        userDTO.setInstitutionId(institution.getInstitutionId());

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setUserId(2L);
        userDTO2.setFirstName("name2");
        userDTO2.setLastName("last name2");
        userDTO2.setEmail("email2");
        userDTO2.setInstitutionId(institution.getInstitutionId());

        when(userRepository.findUsersByInstitution(Mockito.any(Long.class))).thenReturn(Optional.of(mockedUsers));

        List<UserDTO> result = userService.findUsersByInstitution(institutionId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("name", result.get(0).getFirstName());
        assertEquals("name2", result.get(1).getFirstName());
    }
}
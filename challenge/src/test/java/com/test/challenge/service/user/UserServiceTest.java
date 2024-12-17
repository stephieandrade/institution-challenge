package com.test.challenge.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.challenge.dto.InstitutionDTO;
import com.test.challenge.dto.UserDTO;
import com.test.challenge.model.Institution;
import com.test.challenge.model.User;
import com.test.challenge.repository.InstitutionRepository;
import com.test.challenge.repository.UserRepository;
import com.test.challenge.service.UserService;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private UserService userService;

    @Test
    void find() throws BadRequestException {
        Long userId = 1L;

        Institution institution = new Institution();
        institution.setInstitutionId(1L);

        User mockedUser = new User();
        mockedUser.setUserId(userId);
        mockedUser.setFirstName("name");
        mockedUser.setLastName("lastname");
        mockedUser.setInstitution(institution);

        UserDTO mockedUserDto = new UserDTO();
        mockedUserDto.setUserId(userId);
        mockedUserDto.setFirstName("name");
        mockedUserDto.setLastName("lastname");
        mockedUserDto.setInstitutionId(institution.getInstitutionId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockedUser));

        UserDTO result = userService.find(userId);

        assertNotNull(result);
        assertEquals(mockedUserDto.getLastName(), result.getLastName());
        assertEquals(mockedUserDto.getFirstName(), result.getFirstName());
        assertEquals(mockedUserDto.getUserId(), result.getUserId());
        assertEquals(mockedUserDto.getInstitutionId(), result.getInstitutionId());
        assertEquals(mockedUserDto.getEmail(), result.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void save() throws BadRequestException {
        Institution institution = new Institution();
        institution.setInstitutionId(1L);
        institution.setName("Institution 1");

        User mockedUser = new User();
        mockedUser.setUserId(1L);
        mockedUser.setFirstName("name");
        mockedUser.setLastName("last name");
        mockedUser.setEmail("email");
        mockedUser.setInstitution(institution);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setFirstName("name");
        userDTO.setLastName("last name");
        userDTO.setEmail("email");
        userDTO.setInstitutionId(institution.getInstitutionId());

        when(userRepository.save(Mockito.any(User.class))).thenReturn(mockedUser);
        when(institutionRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(institution));

        UserDTO savedUser = userService.save(userDTO);

        assertNotNull(savedUser);
        assertEquals(userDTO.getUserId(), savedUser.getInstitutionId());
        assertEquals(userDTO.getFirstName(), savedUser.getFirstName());
        assertEquals(userDTO.getLastName(), savedUser.getLastName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
    }

    @Test
    void findAll() {
        Institution institution = new Institution();
        institution.setInstitutionId(1L);
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

        when(userRepository.findAll()).thenReturn(mockedUsers);

        List<UserDTO> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("name", result.get(0).getFirstName());
        assertEquals("name2", result.get(1).getFirstName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void update() throws BadRequestException {
        Long userId = 1L;

        Institution institution = new Institution();
        institution.setInstitutionId(1L);
        institution.setName("Institution 1");

        User foundUser = new User();
        foundUser.setUserId(userId);
        foundUser.setFirstName("old-first-name");
        foundUser.setLastName("old-last-name");
        foundUser.setEmail("old-email@email.com");
        foundUser.setInstitution(institution);

        User updatedUser = new User();
        updatedUser.setUserId(userId);
        updatedUser.setFirstName("updated-first-name");
        updatedUser.setLastName("updated-last-name");
        updatedUser.setEmail("updated-email@email.com");
        updatedUser.setInstitution(institution);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setFirstName("updated-first-name");
        userDTO.setLastName("updated-last-name");
        userDTO.setEmail("updated-email@email.com");
        userDTO.setInstitutionId(institution.getInstitutionId());

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(foundUser));
        when(institutionRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(institution));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);
        when(mapper.convertValue(updatedUser, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.update(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getInstitutionId(), result.getInstitutionId());

        verify(userRepository, times(1)).findById(1L);
        verify(institutionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(foundUser);
        verify(mapper, times(1)).convertValue(updatedUser, UserDTO.class);
    }

    @Test
    void update_throwsBadRequest_IfUserDoesNotExist() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.update(userDTO));
        assertEquals("User could not be updated.", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void delete() {
        Long userId = 1L;
        User mockedUser = new User();
        mockedUser.setUserId(userId);
        mockedUser.setFirstName("name");

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockedUser));

        assertAll(()-> userService.delete(userId));
    }

    @Test
    void assignUserToInstitution(){
        Long userId = 1L;
        Long institutionId = 100L;
        boolean isPrimary = true;

        User user = new User();
        user.setUserId(userId);
        user.setInstitutions(new HashSet<>());

        Institution institution = new Institution();
        institution.setInstitutionId(institutionId);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(institutionRepository.findById(institutionId)).thenReturn(Optional.of(institution));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.assignUserToInstitution(userId, institutionId, isPrimary);

        // Assert
        verify(userRepository).findById(userId);
        verify(institutionRepository).findById(institutionId);
        assertTrue(user.getInstitutions().contains(institution), "The institution should be added to the user's institution set.");
        if (isPrimary) {
            assertEquals(institution, user.getInstitution(), "The institution should be set as the primary institution.");
        }
        verify(userRepository).save(user);
    }
}
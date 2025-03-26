package com.intask.Task.Service;

import com.intask.Task.DTO.UserDTO;
import com.intask.Task.Entity.User;
import com.intask.Task.Exceptions.ResourceNotFoundException;
import com.intask.Task.Repository.UserRepo;
import com.intask.Task.TestContainerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @InjectMocks
    private UserServices userServices;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ModelMapper modelMapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");

        user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
    }

    @Test
    void testAddUser_Success() {
        when(userRepo.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        ResponseEntity<?> response = userServices.AddUser(userDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testAddUser_UserAlreadyExists() {
        when(userRepo.existsByEmail(userDTO.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userServices.AddUser(userDTO);
        });

        assertEquals("409 CONFLICT \"User already exists with email : test@example.com\"", exception.getMessage());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepo.existsByEmail(userDTO.getEmail())).thenReturn(true);
        when(userRepo.findByEmail(userDTO.getEmail())).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        ResponseEntity<?> response = userServices.GetUserByEmail(userDTO.getEmail());

        assertEquals(302, response.getStatusCodeValue()); // FOUND = 302
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepo.existsByEmail(userDTO.getEmail())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userServices.GetUserByEmail(userDTO.getEmail());
        });

        assertEquals("User not found with given email : test@example.com", exception.getMessage());
    }
}

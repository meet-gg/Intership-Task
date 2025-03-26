package com.intask.Task.Service;

import com.intask.Task.DTO.UserDTO;
import com.intask.Task.Entity.User;
import com.intask.Task.Exceptions.ResourceNotFoundException;
import com.intask.Task.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> AddUser(UserDTO user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with email : " + user.getEmail());
        }
        User user1 = modelMapper.map(user, User.class);
        User saveUser = userRepo.save(user1);
        return new ResponseEntity<>(modelMapper.map(saveUser,UserDTO.class), HttpStatus.CREATED);
    }
    public ResponseEntity<?> GetUserByEmail(String email) {
        if (userRepo.existsByEmail(email)) {
            User byEmail = userRepo.findByEmail(email);
            UserDTO userDTO = modelMapper.map(byEmail, UserDTO.class);
            return new ResponseEntity<>(userDTO, HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException(HttpStatus.NOT_FOUND,"User not found with given email : "+email);
    }

}

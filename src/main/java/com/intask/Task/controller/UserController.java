package com.intask.Task.controller;

import com.intask.Task.DTO.InsuranceDTO;
import com.intask.Task.DTO.UserDTO;
import com.intask.Task.Service.InsuranceService;
import com.intask.Task.Service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    public final UserServices userServices;
    private final InsuranceService insuranceService;

    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        ResponseEntity<?> saveUser = userServices.AddUser(userDTO);
        return ResponseEntity.ok(saveUser);
    }
    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        ResponseEntity<?> UserDetail = userServices.GetUserByEmail(email);
        return ResponseEntity.ok(UserDetail);
    }
    @GetMapping("/curated/{userId}")
    public ResponseEntity<List<InsuranceDTO>> getCuratedInsurances(@PathVariable Long userId) {
        return insuranceService.getCuratedInsurances(userId);
    }

}

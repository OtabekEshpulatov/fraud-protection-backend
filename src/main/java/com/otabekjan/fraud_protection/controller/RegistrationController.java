package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.dto.RegisterRequestDto;
import com.otabekjan.fraud_protection.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok().build();
    }
}

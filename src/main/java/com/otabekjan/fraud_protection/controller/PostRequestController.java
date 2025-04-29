package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.dto.PostRequestDto;
import com.otabekjan.fraud_protection.service.PostRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class PostRequestController {

    private final PostRequestService postRequestService;

    @PostMapping
    public ResponseEntity<?> request(@Valid @RequestBody PostRequestDto dto) {
        postRequestService.makeRequest(dto);
        return ResponseEntity.ok().build();
    }
}

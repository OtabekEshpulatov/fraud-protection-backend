package com.otabekjan.fraud_protection.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(@NotBlank String username, @NotBlank String password, String locale,
                                 String profilePhotoId) {
}
    
package com.otabekjan.fraud_protection.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RegisterRequestDto(@NotBlank String username,
                                 @NotBlank String password,
                                 String locale,
                                 String profilePhotoId,
                                 UUID regionId,
                                 @NotBlank String appleDeviceToken) {
}
    
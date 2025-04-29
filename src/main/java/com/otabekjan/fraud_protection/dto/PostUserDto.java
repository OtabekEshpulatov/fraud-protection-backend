package com.otabekjan.fraud_protection.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PostUserDto {
    private UUID id;
    private String username;
    private Boolean verified;
    private String profilePhotoUrl;
}

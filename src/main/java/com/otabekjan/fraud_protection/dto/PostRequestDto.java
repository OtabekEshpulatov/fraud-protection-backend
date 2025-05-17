package com.otabekjan.fraud_protection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotEmpty
    private List<String> mediaIds;

    @NotNull
    private UUID regionId;

}

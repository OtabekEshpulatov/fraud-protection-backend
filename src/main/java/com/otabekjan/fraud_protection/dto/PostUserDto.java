package com.otabekjan.fraud_protection.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class PostUserDto implements Serializable {


    private static final long serialVersionUID = 1853107880231157818L;
    private UUID id;
    private String username;
    private Boolean verified;
    private String profilePhotoUrl;
}

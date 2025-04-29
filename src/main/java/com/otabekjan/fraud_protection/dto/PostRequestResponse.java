package com.otabekjan.fraud_protection.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PostRequestResponse {

    private UUID requestId;
    private boolean ok;
    private String friendlyMessage;
}

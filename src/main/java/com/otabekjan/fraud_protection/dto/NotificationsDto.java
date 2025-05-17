package com.otabekjan.fraud_protection.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationsDto {

    private final UUID id;
    private final String title;
    private final String body;
    private final String payload;
}

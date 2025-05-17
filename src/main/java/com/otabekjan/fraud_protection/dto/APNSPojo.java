package com.otabekjan.fraud_protection.dto;

import com.otabekjan.fraud_protection.enums.NotificationType;
import lombok.Data;

import java.io.Serializable;

@Data
public class APNSPojo implements Serializable {


    private static final long serialVersionUID = -6446152775151386165L;
    private final String title;
    private String body;
    private final NotificationType type;
    private final Object payload;
    private final Integer badge;
    private final String deviceToken;
}

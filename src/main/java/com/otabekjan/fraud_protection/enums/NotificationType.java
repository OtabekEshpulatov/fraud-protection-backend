package com.otabekjan.fraud_protection.enums;

import io.jmix.core.metamodel.datatype.EnumClass;
import lombok.Getter;

public enum NotificationType implements EnumClass<String> {

    POST("POST");

    @Getter
    private String id;

    NotificationType(String id) {
        this.id = id;
    }

    public static NotificationType fromId(String id) {
        for (NotificationType value : values()) {
            if (value.getId().equals(id)) return value;
        }
        return null;
    }
}

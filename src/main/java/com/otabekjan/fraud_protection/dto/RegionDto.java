package com.otabekjan.fraud_protection.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RegionDto {

    private final UUID id;
    private final List<RegionLocalizedName> names;

    @Data
    public static class RegionLocalizedName {
        private final String locale;
        private final String name;
    }
}

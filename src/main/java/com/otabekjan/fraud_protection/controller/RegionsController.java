package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.dto.RegionDto;
import com.otabekjan.fraud_protection.entity.Region;
import com.otabekjan.fraud_protection.entity.TranslateEntity;
import com.otabekjan.fraud_protection.service.RegionService;
import com.otabekjan.fraud_protection.service.TranslateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/regions")
@RequiredArgsConstructor
public class RegionsController {

    private final RegionService regionService;
    private final TranslateService translateService;

    @GetMapping
    public ResponseEntity<List<RegionDto>> getRegions() {
        List<Region> regions = regionService.getRegions();
        List<RegionDto> regionsResponseDto = new ArrayList<>(regions.size());
        for (Region region : regions) {
            List<TranslateEntity> allTranslations = translateService.getAllTranslations(region, "name");
            List<RegionDto.RegionLocalizedName> localizedNames = new ArrayList<>(allTranslations.size());
            for (TranslateEntity translation : allTranslations) {
                localizedNames.add(new RegionDto.RegionLocalizedName(translation.getLocale(), translation.getTranslated()));
            }
            RegionDto regionDto = new RegionDto(region.getId(), localizedNames);
            regionsResponseDto.add(regionDto);
        }

        return ResponseEntity.ok(regionsResponseDto);

    }
}

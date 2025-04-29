package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.entity.Region;
import io.jmix.core.DataManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final DataManager dataManager;

    public List<Region> getRegions() {
        return dataManager.load(Region.class)
                .query("select e from Region e where e.active = true")
                .list();
    }
}

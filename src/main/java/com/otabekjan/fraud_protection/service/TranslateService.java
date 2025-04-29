package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.entity.AppEntity;
import com.otabekjan.fraud_protection.entity.TranslateEntity;
import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslateService {


    private final DataManager dataManager;
    private final Metadata metadata;


    @Cacheable(cacheNames = "TRANSLATE-CACHE", key = "#entity.getId() + #field + #locale")
    public String translate(AppEntity<UUID> entity, String field, String locale, String source) {
        TranslateEntity translateEntity = dataManager.load(TranslateEntity.class)
                .query("select e from TranslateEntity  e where e.entityId = :entityId " +
                        "and e.entityClass = :entityClass " +
                        "and e.locale = :locale " +
                        "and e.entityField = :entityField")
                .parameter("entityId", entity.getId())
                .parameter("entityClass", metadata.getClass(entity).getName())
                .parameter("entityField", field)
                .parameter("locale", locale)
                .maxResults(1)
                .optional().orElse(null);

        if (translateEntity != null) {
            return translateEntity.getTranslated();
        }

        return source;
    }

    @Cacheable(cacheNames = "TRANSLATE-CACHE", key = "#entity.getId() + #field")
    public List<TranslateEntity> getAllTranslations(AppEntity<UUID> entity, String field) {
        return dataManager.load(TranslateEntity.class)
                .query("select e from TranslateEntity  e where e.entityId = :entityId " +
                        "and e.entityClass = :entityClass " +
                        "and e.entityField = :entityField")
                .parameter("entityId", entity.getId())
                .parameter("entityClass", metadata.getClass(entity).getName())
                .parameter("entityField", field)
                .list();
    }
}

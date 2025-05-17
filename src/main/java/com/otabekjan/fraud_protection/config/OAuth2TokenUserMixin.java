package com.otabekjan.fraud_protection.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.otabekjan.fraud_protection.entity.Region;
import io.jmix.authserver.service.mapper.DefaultOAuth2TokenUserMixin;
import io.jmix.core.FileRef;

public class OAuth2TokenUserMixin extends DefaultOAuth2TokenUserMixin {

    @JsonIgnore
    private FileRef profilePhoto;

    @JsonIgnore
    private Region region;
}
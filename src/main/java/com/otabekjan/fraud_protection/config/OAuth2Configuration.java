package com.otabekjan.fraud_protection.config;

import com.otabekjan.fraud_protection.entity.User;
import io.jmix.authserver.service.mapper.JdbcOAuth2AuthorizationServiceObjectMapperCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Configuration {
    @Bean
    JdbcOAuth2AuthorizationServiceObjectMapperCustomizer tokenObjectMapperCustomizer() {
        return objectMapper ->
                objectMapper.addMixIn(User.class, OAuth2TokenUserMixin.class);
    }
}

package com.otabekjan.fraud_protection.config;

import com.otabekjan.fraud_protection.component.translation.*;
import io.jmix.flowui.sys.registration.ComponentRegistration;
import io.jmix.flowui.sys.registration.ComponentRegistrationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {


    @Bean
    public ComponentRegistration translateField() {
        return ComponentRegistrationBuilder.create(TranslateField.class)
                .withComponentLoader("translateField", TranslateFieldLoader.class)
                .build();
    }

    @Bean
    public ComponentRegistration translateAreaField() {
        return ComponentRegistrationBuilder.create(TranslateAreaField.class)
                .withComponentLoader("translateAreaField", TranslateAreaFieldLoader.class)
                .build();
    }

    @Bean
    public ComponentRegistration translateRichTextEditor() {
        return ComponentRegistrationBuilder.create(TranslateRichTextEditor.class)
                .withComponentLoader("translateRichTextEditor", TranslateRichTextEditorLoader.class)
                .build();
    }
}

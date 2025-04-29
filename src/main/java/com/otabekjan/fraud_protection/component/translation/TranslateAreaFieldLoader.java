package com.otabekjan.fraud_protection.component.translation;


import io.jmix.flowui.xml.layout.loader.component.TextAreaLoader;

/**
 * Author: abdul
 * Since: 12/7/2024 12:28 PM
 */
public class TranslateAreaFieldLoader extends TextAreaLoader {

    @Override
    protected TranslateAreaField createComponent() {
        return factory.create(TranslateAreaField.class);
    }
}

package com.otabekjan.fraud_protection.component.translation;


import io.jmix.flowui.xml.layout.loader.component.TextFieldLoader;

/**
 * Author: abdul
 * Since: 12/6/2024 5:59 PM
 */
public class TranslateFieldLoader extends TextFieldLoader {

    @Override
    protected TranslateField createComponent() {
        return factory.create(TranslateField.class);
    }
}

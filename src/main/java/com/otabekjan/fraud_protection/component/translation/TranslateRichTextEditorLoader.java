package com.otabekjan.fraud_protection.component.translation;


import io.jmix.flowui.xml.layout.loader.component.RichTextEditorLoader;

/**
 * Author: abdul
 * Since: 12/7/2024 12:43 PM
 */
public class TranslateRichTextEditorLoader extends RichTextEditorLoader {

    @Override
    protected TranslateRichTextEditor createComponent() {
        return factory.create(TranslateRichTextEditor.class);
    }
}

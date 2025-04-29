package com.otabekjan.fraud_protection.component.translation.view;


import com.otabekjan.fraud_protection.entity.TranslateEntity;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.fragment.Fragment;
import lombok.Getter;

import java.util.Locale;

/**
 * Author: abdul
 * Since: 12/11/2024 12:03 PM
 */

@Getter
public abstract class AbstractTranslationFragment extends Fragment<VerticalLayout> {

    private TranslateEntity translation;

    private Locale language;

    public void init(TranslateEntity translation, Locale language) {
        this.translation = translation;
        this.language = language;
    }

    public boolean defaultLocale() {
        return language != null &&
                Locale.forLanguageTag("ru").equals(language);
    }
}

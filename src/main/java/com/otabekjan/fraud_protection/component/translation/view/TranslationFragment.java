package com.otabekjan.fraud_protection.component.translation.view;

import com.otabekjan.fraud_protection.entity.TranslateEntity;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.ViewComponent;

import java.util.Locale;

@FragmentDescriptor("translation-fragment.xml")
public class TranslationFragment extends AbstractTranslationFragment {

    @ViewComponent
    private InstanceContainer<TranslateEntity> translationDc;
    @ViewComponent
    private JmixTextArea translatedField;

    @Override
    public void init(TranslateEntity translation, Locale language) {
        super.init(translation, language);

        translationDc.unmute();
        translationDc.setItem(translation);

        if (defaultLocale())
            translatedField.setValue(translation.getSource());
    }
}
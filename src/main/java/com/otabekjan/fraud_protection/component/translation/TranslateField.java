package com.otabekjan.fraud_protection.component.translation;


import com.otabekjan.fraud_protection.AppBeans;
import com.otabekjan.fraud_protection.component.translation.view.TranslationView;
import com.otabekjan.fraud_protection.entity.AppEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.data.ValueSource;
import io.jmix.flowui.data.value.ContainerValueSource;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.StandardOutcome;
import lombok.Setter;

import java.util.UUID;

/**
 * Author: abdul
 * Since: 12/6/2024 5:58 PM
 */
public class TranslateField extends TypedTextField<String> {

    private boolean addedListener = false;

    @Setter
    private AppEntity<UUID> entity;
    @Setter
    private String fieldName;
    @Setter
    private String source;

    public TranslateField() {
        JmixButton button = AppBeans.get(UiComponents.class).create(JmixButton.class);
        button.setIcon(new Icon(VaadinIcon.TEXT_LABEL));
        button.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(this::onBtnClickListener);
        setSuffixComponent(button);

        addValueChangeListener(event -> setSource(event.getValue()));
    }

    public void onBtnClickListener(ClickEvent<?> listener) {
        DialogWindow<TranslationView> dialogWindow = AppBeans.get(DialogWindows.class)
                .view(UiComponentUtils.getView(this), TranslationView.class)
                .withViewConfigurer(view -> {
                    view.setEntity(entity);
                    view.setFieldName(fieldName);
                    view.setSource(source);
                })
                .withAfterCloseListener(event -> {
                    if (event.closedWith(StandardOutcome.SAVE)) {
                        setValue(event.getView().getTranslate().getTranslated());
                    }
                })
                .build();
        dialogWindow.addClassName("translation-dialog");
        dialogWindow.open();
    }

    @Override
    public void setValueSource(ValueSource<String> valueSource) {
        super.setValueSource(valueSource);
        if (valueSource instanceof ContainerValueSource) {
            if (!addedListener) {
                ((ContainerValueSource<AppEntity<UUID>, String>) valueSource).addInstanceChangeListener(instanceChangeEvent -> {
                    setEntity(instanceChangeEvent.getItem());
                    setSource(valueSource.getValue());
                    try {
                        fieldName = ((ContainerValueSource<AppEntity<UUID>, String>) valueSource).getMetaPropertyPath().getFirstPropertyName();
                    } catch (Exception ignored) {
                    }
                });
                addedListener = true;
            }
        }
    }
}

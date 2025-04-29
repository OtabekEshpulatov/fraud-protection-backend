package com.otabekjan.fraud_protection.component.translation.view;

import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.entity.AppEntity;
import com.otabekjan.fraud_protection.entity.TranslateEntity;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.core.Metadata;
import io.jmix.core.SaveContext;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.richtexteditor.JmixRichTextEditor;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Route(value = "translation-view", layout = MainView.class)
@ViewController(id = "jb_TranslationView")
@ViewDescriptor(path = "translation-view.xml")
@DialogMode(width = "40em")
public class TranslationView extends StandardView {
    private final List<AbstractTranslationFragment> fragmentList = new ArrayList<>();
    @Getter
    private TranslateEntity translate;
    @Setter
    private AppEntity<UUID> entity;
    @Setter
    private String fieldName;
    @Setter
    private String source;
    @Setter
    private Boolean rich;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Fragments fragments;
    @Autowired
    private Metadata metadata;
    @Autowired
    private UiComponents uiComponents;
    @ViewComponent
    private JmixTabSheet tabSheet;
    @ViewComponent
    private CollectionLoader<TranslateEntity> translationDl;
    private List<Locale> languages;

    public static String countryFlag(Locale locale) {
        String code = locale.getCountry(); // Get the country code

        if (code.isEmpty()) {
            code = locale.getLanguage().toUpperCase(); // Assign language tag if no country
        }

        if (code.isEmpty()) {
            return "\uD83C\uDF10"; // No country code, no flag
        }

        // offset between uppercase ASCII and regional indicator symbols
        int OFFSET = 127397;

        StringBuilder emojiStr = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            emojiStr.appendCodePoint(code.charAt(i) + OFFSET);
        }
        return emojiStr.toString();
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        this.languages = List.of(Locale.forLanguageTag("en"),
                Locale.forLanguageTag("uz"),
                Locale.forLanguageTag("ru"));
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        if (entity != null) translationDl.setParameter("entityId", entity.getId());
        if (fieldName != null) translationDl.setParameter("entityField", fieldName);
        translationDl.load();
    }

    @Subscribe(id = "translationDl", target = Target.DATA_LOADER)
    public void onTranslationDlPostLoad(final CollectionLoader.PostLoadEvent<TranslateEntity> event) {
        List<TranslateEntity> loadedEntities = event.getLoadedEntities();
        for (Locale language : languages) {
            String locale = language.getLanguage();
            TranslateEntity translation = loadedEntities.stream()
                    .filter(tr -> Objects.equals(locale, tr.getLocale()))
                    .findFirst().orElse(null);
            if (translation == null) {
                translation = dataManager.create(TranslateEntity.class);
                translation.setLocale(locale);
                translation.setEntityClass(metadata.getClass(entity).getName());
                translation.setEntityId(entity.getId());
                translation.setEntityField(fieldName);
            }
            translation.setSource(source);

            if ("ru".equals(language.getLanguage())) defField(translation, language);
            else buildTab(translation, language);
        }
    }

    private void defField(TranslateEntity translation, Locale language) {
        this.translate = translation;

        if (Boolean.TRUE.equals(rich)) {
            JmixRichTextEditor textEditor = uiComponents.create(JmixRichTextEditor.class);
            textEditor.setLabel(countryFlag(language));
            textEditor.addClassName("def-translation-field");
            textEditor.addValueChangeListener(event ->
                    translate.setTranslated(event.getValue()));
            textEditor.setValue(source);
            getContent().addComponentAsFirst(textEditor);
        } else {
            JmixTextArea textArea = uiComponents.create(JmixTextArea.class);
            textArea.setLabel(countryFlag(language));
            textArea.addClassName("def-translation-field");
            textArea.addValueChangeListener(event ->
                    translate.setTranslated(event.getValue()));
            textArea.setValue(source);
            getContent().addComponentAsFirst(textArea);
        }
    }

    private void buildTab(TranslateEntity translation, Locale language) {
        AbstractTranslationFragment fragment = Boolean.TRUE.equals(rich)
                ? fragments.create(this, RichTranslationFragment.class)
                : fragments.create(this, TranslationFragment.class);
        fragment.init(translation, language);

        tabSheet.add(countryFlag(language), fragment);
        fragmentList.add(fragment);
    }

    @Subscribe(id = "cancelBtn", subject = "clickListener")
    public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
        closeWithDefaultAction();
    }

    @Subscribe(id = "saveBtn", subject = "clickListener")
    public void onSaveBtnClick(final ClickEvent<JmixButton> event) {
        SaveContext context = new SaveContext();
        validateTranslate(context, translate);

        for (AbstractTranslationFragment fragment : fragmentList) {
            TranslateEntity translation = fragment.getTranslation();
            validateTranslate(context, translation);
        }
        dataManager.save(context);
        close(StandardOutcome.SAVE);
    }

    private void validateTranslate(SaveContext context, TranslateEntity translation) {
        if (translation == null) return;
        if ($.isEmpty(translation.getTranslated())) {
            if (!entityStates.isNew(translation))
                context.removing(translation);
            return;
        }
        context.saving(translation);
    }
}
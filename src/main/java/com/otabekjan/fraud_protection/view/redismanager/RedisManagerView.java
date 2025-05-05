package com.otabekjan.fraud_protection.view.redismanager;


import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.ComponentUtils;
import com.otabekjan.fraud_protection.service.GeneralCacheManagerService;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.KeyValueCollectionContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Route(value = "RedisManagerView", layout = MainView.class)
@ViewController("jb_RedisManagerView")
@ViewDescriptor("redis-manager-view.xml")
public class RedisManagerView extends StandardView {
    @Autowired
    protected DataManager dataManager;
    private Map<String, Collection<String>> cacheNames;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private GeneralCacheManagerService generalCacheManagerService;

    @Autowired
    private Messages messages;

    @ViewComponent
    private MessageBundle messageBundle;
    @ViewComponent
    private KeyValueCollectionContainer cachesDc;
    @ViewComponent
    private JmixSelect<String> cacheManagersSelect;
    @ViewComponent
    private DataGrid<KeyValueEntity> cachesDataGrid;

    @Subscribe
    public void onInit(final View.InitEvent event) {
        cacheNames = generalCacheManagerService.getAllCacheNames();

        if (!cacheNames.isEmpty()) {
            List<String> sorted = cacheNames.keySet().stream().sorted((o1, o2) ->
                    $.compare(cacheNames.get(o2).size(), cacheNames.get(o1).size())).toList();
            cacheManagersSelect.setItems(sorted);
            cacheManagersSelect.setValue(sorted.get(0));
        }
    }

    @Subscribe("cacheManagersSelect")
    public void onCacheManagersSelectComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixSelect<String>, String> event) {
        List<KeyValueEntity> entities = new ArrayList<>();
        String cacheManager = event.getValue();
        for (String cacheName : cacheNames.get(cacheManager)) {
            KeyValueEntity entity = dataManager.create(KeyValueEntity.class);
            entity.setValue("cacheManager", cacheManager);
            entity.setValue("cacheName", cacheName);
            entities.add(entity);
        }
        cachesDc.setItems(entities);
    }

    @Subscribe("cachesDataGrid.clear")
    public void onCachesDataGridClear(final ActionPerformedEvent event) {
        Collection<KeyValueEntity> caches = cachesDataGrid.getSelectedItems();
        String optionText = messageBundle.getMessage("Delete all selected");

        if ($.isEmpty(caches)) {
            caches = cachesDc.getItems();
            optionText = messageBundle.getMessage("Delete all caches");
        }

        Collection<KeyValueEntity> finalCaches = caches;
        dialogs.createOptionDialog()
                .withHeader(messages.getMessage("Cache deletion"))
                .withText(optionText)
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withHandler(e -> clearCache(finalCaches)),
                        new DialogAction(DialogAction.Type.NO)
                )
                .open();
    }

    private void clearCache(Collection<KeyValueEntity> entities) {
        for (KeyValueEntity entity : entities) {
            boolean success = false;
            String message;

            try {
                String cacheManager = entity.getValue("cacheManager");
                String cacheName = entity.getValue("cacheName");
                success = generalCacheManagerService.clearCache(cacheManager, cacheName);
                String fullCacheName = cacheManager + "." + cacheName;
                message = !success ? "Cannot clear cache %s".formatted(fullCacheName) : ("Cache cleared %s".formatted(fullCacheName));
            } catch (GeneralCacheManagerService.IllegalCacheAccessException ex) {
                message = ex.getMessage();
            }

            if (success) ComponentUtils.notifySuccess(message).open();
            else ComponentUtils.notifyError(message).open();
        }
    }
}
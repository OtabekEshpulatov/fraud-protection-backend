package com.otabekjan.fraud_protection.view.appnotification;

import com.otabekjan.fraud_protection.entity.AppNotification;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.action.SecuredBaseAction;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.Action;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;

@Route(value = "app-notifications", layout = MainView.class)
@ViewController(id = "AppNotification.list")
@ViewDescriptor(path = "app-notification-list-view.xml")
@LookupComponent("appNotificationsDataGrid")
@DialogMode(width = "64em")
public class AppNotificationListView extends StandardListView<AppNotification> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<AppNotification> appNotificationsDc;

    @ViewComponent
    private InstanceContainer<AppNotification> appNotificationDc;

    @ViewComponent
    private InstanceLoader<AppNotification> appNotificationDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private DataGrid<AppNotification> appNotificationsDataGrid;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onInit(final InitEvent event) {
        appNotificationsDataGrid.getActions().forEach(action -> {
            if (action instanceof SecuredBaseAction secured) {
                secured.addEnabledRule(() -> listLayout.isEnabled());
            }
        });
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("appNotificationsDataGrid.createAction")
    public void onAppNotificationsDataGridCreateAction(final ActionPerformedEvent event) {
        dataContext.clear();
        AppNotification entity = dataContext.create(AppNotification.class);
        appNotificationDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("appNotificationsDataGrid.editAction")
    public void onAppNotificationsDataGridEditAction(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        AppNotification item = appNotificationDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        appNotificationsDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        appNotificationDc.setItem(null);
        appNotificationDl.load();
        updateControls(false);
    }

    @Subscribe(id = "appNotificationsDc", target = Target.DATA_CONTAINER)
    public void onAppNotificationsDcItemChange(final InstanceContainer.ItemChangeEvent<AppNotification> event) {
        AppNotification entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            appNotificationDl.setEntityId(entity.getId());
            appNotificationDl.load();
        } else {
            appNotificationDl.setEntityId(null);
            appNotificationDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(AppNotification entity) {
        ViewValidation viewValidation = getViewValidation();
        ValidationErrors validationErrors = viewValidation.validateUiComponents(form);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        validationErrors.addAll(viewValidation.validateBeanGroup(UiCrossFieldChecks.class, entity));
        return validationErrors;
    }

    private void updateControls(boolean editing) {
        UiComponentUtils.getComponents(form).forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
        appNotificationsDataGrid.getActions().forEach(Action::refreshState);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }
}
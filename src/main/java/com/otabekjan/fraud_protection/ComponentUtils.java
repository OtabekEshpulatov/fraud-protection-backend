package com.otabekjan.fraud_protection;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import io.jmix.core.Messages;
import io.jmix.flowui.Notifications;

public class ComponentUtils {

    public static Notification notifyError(String message) {
        return notify(message, NotificationVariant.LUMO_ERROR);
    }

    public static Notification notifyWarning(String message) {
        return notify(message, NotificationVariant.LUMO_WARNING);
    }

    public static Notification notifySuccess(String message) {
        return notify(message, NotificationVariant.LUMO_SUCCESS);
    }

    private static Notification notify(String message, NotificationVariant variant) {
        String title = AppBeans.get(Messages.class).getMessage("Attention");
        return AppBeans.get(Notifications.class)
                .create(title, message)
                .withPosition(Notification.Position.TOP_END)
                .withThemeVariant(variant)
                .withDuration(3000)
                .build();
    }
}

package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.dto.NotificationsDto;
import com.otabekjan.fraud_protection.entity.AppNotification;
import com.otabekjan.fraud_protection.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final CurrentAuthentication currentAuthentication;
    private final DataManager dataManager;


    @NonNull
    public Long countUnReadNotifications() {
        if (currentAuthentication.getUser() instanceof User user) {
            return countUnReadNotifications(user);
        }
        return 0L;
    }

    @NonNull
    public Long countUnReadNotifications(User user) {
        return dataManager.loadValue("select count(e) from AppNotification e where e.user = :user and coalesce(e.read,false) = false", Long.class)
                .parameter("user", user)
                .optional().orElse(0L);
    }

    @NotNull
    public void read(UUID id) {
        if (currentAuthentication.getUser() instanceof User user) {
            var appNotification = dataManager.load(AppNotification.class)
                    .query("select e from AppNotification e where" +
                            " e.user = :user" +
                            " and e.id = :id" +
                            " and coalesce(e.read, false) = false")
                    .parameter("id", id)
                    .parameter("user", user)
                    .optional().orElse(null);

            if (appNotification != null) {
                appNotification.setRead(true);
                dataManager.save(appNotification);
            }
        }
    }

    public Collection<NotificationsDto> getAll() {
        if (currentAuthentication.getUser() instanceof User user) {
            List<AppNotification> notifications = dataManager.load(AppNotification.class)
                    .query("select e from AppNotification e where" +
                            " e.user = :user" +
                            " and coalesce(e.read, false) = false")
                    .parameter("user", user)
                    .list();


            return notifications.stream()
                    .map(appNotification -> new NotificationsDto(appNotification.getId(), appNotification.getTitle(),
                            appNotification.getBody(), appNotification.getPayload()))
                    .toList();
        }

        return List.of();
    }
}

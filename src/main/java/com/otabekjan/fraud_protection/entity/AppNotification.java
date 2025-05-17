package com.otabekjan.fraud_protection.entity;

import com.otabekjan.fraud_protection.enums.NotificationType;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JmixEntity
@Table(name = "APP_NOTIFICATION")
@Entity
@Getter
@Setter
public class AppNotification extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "READ")
    private Boolean read;

    @Column(name = "NOTIFICATION_TYPE")
    private String notificationType;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "BODY")
    private String body;

    @Lob
    @Column(name = "PAYLOAD")
    private String payload;

    public void setNotificationType(NotificationType type) {
        this.notificationType = type == null ? null : type.getId();
    }

    public NotificationType getNotificationType() {
        return notificationType == null ? null : NotificationType.fromId(notificationType);
    }
}
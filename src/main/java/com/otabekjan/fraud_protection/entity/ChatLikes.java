package com.otabekjan.fraud_protection.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

@JmixEntity
@Table(name = "CHAT_LIKES", indexes = {
        @Index(name = "IDX_CHAT_LIKES_USER", columnList = "USER_ID"),
        @Index(name = "IDX_CHAT_LIKES_MESSAGE", columnList = "MESSAGE_ID")
})
@Entity
public class ChatLikes extends AbstractEntity {

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "MESSAGE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatMessage message;

    @Column(name = "LIKE")
    private Boolean like;
}
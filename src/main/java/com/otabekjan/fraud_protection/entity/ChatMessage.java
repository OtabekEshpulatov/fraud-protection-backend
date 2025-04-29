package com.otabekjan.fraud_protection.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;

@JmixEntity
@Table(name = "CHAT_MESSAGE", indexes = {
        @Index(name = "IDX_CHAT_MESSAGE_CHAT", columnList = "CHAT_ID"),
        @Index(name = "IDX_CHAT_MESSAGE_USER", columnList = "USER_ID")
})
@Entity
public class ChatMessage extends AbstractEntity {

    @JoinColumn(name = "CHAT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "LIKES")
    private Long likes;

    @Column(name = "DISLIKES")
    private Long dislikes;

}
package com.otabekjan.fraud_protection.entity;

import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JmixEntity
@Table(name = "CHAT", indexes = {
        @Index(name = "IDX_CHAT_POST", columnList = "POST_ID")
})
@Entity
@Getter
@Setter
public class Chat extends AbstractEntity {

    @JoinColumn(name = "POST_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private Post post;

    @OrderBy(value = "createdDate")
    @Composition
    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages;

    @Column(name = "MESSAGES_COUNT")
    private Long messagesCount;

}
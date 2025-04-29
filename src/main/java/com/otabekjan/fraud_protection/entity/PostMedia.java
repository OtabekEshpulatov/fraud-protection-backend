package com.otabekjan.fraud_protection.entity;

import io.jmix.core.FileRef;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JmixEntity
@Table(name = "POST_MEDIA", indexes = {
        @Index(name = "IDX_POST_MEDIA_POST", columnList = "POST_ID")
})
@Entity
@Getter
@Setter
public class PostMedia extends AbstractEntity {

    @JoinColumn(name = "POST_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "MEDIA", length = 1024)
    private FileRef media;
}
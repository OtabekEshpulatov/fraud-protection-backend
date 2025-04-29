package com.otabekjan.fraud_protection.entity;

import io.jmix.core.FileRef;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@JmixEntity
@Table(name = "POST_REQUEST_MEDIA", indexes = {
        @Index(name = "IDX_POST_REQUEST_MEDIA_REQUEST", columnList = "REQUEST_ID")
})
@Entity
@Getter
@Setter
public class PostRequestMedia extends AbstractEntity {

    @JoinColumn(name = "REQUEST_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostRequest request;

    @Column(name = "SORT")
    private Integer sort;

    @Column(name = "MEDIA", length = 1024)
    private FileRef media;

}
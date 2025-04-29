package com.otabekjan.fraud_protection.entity;

import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JmixEntity
@Table(name = "POST", indexes = {
        @Index(name = "IDX_POST_USER", columnList = "USER_ID"),
        @Index(name = "IDX_POST_REGION", columnList = "REGION_ID")
})
@Entity
@Getter
@Setter
public class Post extends AbstractEntity {

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "ACTIVE")
    private Boolean active;

    @InstanceName
    @Column(name = "TITLE")
    private String title;

    @Column(name = "BODY_")
    @Lob
    private String body;

    @Column(name = "TAGS")
    @Lob
    private String tags;

    @Column(name = "VIEWS")
    private Long views;

    @JoinColumn(name = "REGION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post")
    private Chat chat;

    @OrderBy(value = "sort")
    @Composition
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<PostMedia> medias;
}
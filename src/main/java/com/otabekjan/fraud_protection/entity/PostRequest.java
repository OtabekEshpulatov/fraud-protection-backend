package com.otabekjan.fraud_protection.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@JmixEntity
@Table(name = "POST_REQUEST", indexes = {
        @Index(name = "IDX_POST_REQUEST_USER", columnList = "USER_ID"),
        @Index(name = "IDX_POST_REQUEST_REGION", columnList = "REGION_ID"),
        @Index(name = "IDX_POST_REQUEST_APPROVED_BY", columnList = "APPROVED_BY_ID")
})
@Entity
@Getter
@Setter
public class PostRequest extends AbstractEntity {

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(DeletePolicy.UNLINK)
    private User user;

    @InstanceName
    @Column(name = "TITLE")
    private String title;

    @Column(name = "BODY_")
    @Lob
    private String body;

    @JoinColumn(name = "REGION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;

    @Column(name = "APPROVE_DATE")
    private OffsetDateTime approveDate;

    @OnDelete(DeletePolicy.UNLINK)
    @JoinColumn(name = "APPROVED_BY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User approvedBy;

    @OrderBy(value = "sort")
    @Composition
    @OneToMany(mappedBy = "request", cascade = CascadeType.PERSIST)
    @OnDelete(DeletePolicy.UNLINK)
    private List<PostRequestMedia> medias;
}
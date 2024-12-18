package com.otabekjan.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JmixEntity
@MappedSuperclass
public class AbstractArticle extends AbstractEntity {

    @InstanceName
    @Column(name = "TITLE")
    @Lob
    private String title;

    @Column(name = "OVER_VIEW", length = 1024)
    private String overView;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @JoinColumn(name = "AUTHOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(name = "READ_TIMES")
    private Long readTimes;

    @Column(name = "LIKES")
    private Long likes;
}
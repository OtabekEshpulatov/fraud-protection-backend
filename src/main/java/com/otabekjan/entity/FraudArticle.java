package com.otabekjan.entity;

import io.jmix.core.FileRef;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JmixEntity
@Table(name = "FRAUD_ARTICLE")
@Entity
public class FraudArticle extends AbstractArticle {

    @Column(name = "MARKDOWN_BODY")
    @Lob
    private String markdownBody;

    @Column(name = "OVERVIEW_MEDIA", length = 1024)
    private FileRef overviewMedia;

    @Column(name = "OVERVIEW_MEDIA_TYPE")
    private String overviewMediaType;

    public FileType getOverviewMediaType() {
        return overviewMediaType == null ? null : FileType.fromId(overviewMediaType);
    }

    public void setOverviewMediaType(FileType overviewMediaType) {
        this.overviewMediaType = overviewMediaType == null ? null : overviewMediaType.getId();
    }
}
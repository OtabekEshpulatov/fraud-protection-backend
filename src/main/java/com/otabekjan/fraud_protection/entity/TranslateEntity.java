package com.otabekjan.fraud_protection.entity;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JmixEntity
@Table(name = "TRANSLATE_ENTITY")
@Entity
@Getter
@Setter
public class TranslateEntity extends AbstractEntity {

    private static final long serialVersionUID = 6788163797525469803L;

    @Column(name = "ENTITY_CLASS")
    private String entityClass;

    @Column(name = "ENTITY_ID")
    private UUID entityId;

    @Column(name = "ENTITY_FIELD", length = 1024)
    private String entityField;

    @Column(name = "SOURCE")
    @Lob
    private String source;

    @InstanceName
    @Column(name = "TRANSLATED")
    @Lob
    private String translated;

    @Column(name = "LOCALE")
    private String locale;

}
package com.otabekjan.fraud_protection.entity;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@JmixEntity
@Table(name = "REGION")
@Entity
@Getter
@Setter
public class Region extends AbstractEntity implements HasName {

    @InstanceName
    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "SORT")
    private Integer sort;

}
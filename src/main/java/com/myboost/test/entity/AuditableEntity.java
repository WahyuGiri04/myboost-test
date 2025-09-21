package com.myboost.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_datetime", nullable = false)
    private OffsetDateTime createdDatetime;

    @Column(name = "updated_datetime", nullable = false)
    private OffsetDateTime updatedDatetime;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        this.createdDatetime = now;
        this.updatedDatetime = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDatetime = OffsetDateTime.now(ZoneOffset.UTC);
    }
}

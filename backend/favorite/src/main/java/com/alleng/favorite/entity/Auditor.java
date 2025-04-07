package com.alleng.favorite.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditor {

    @CreatedBy
    protected String createBy;

    @LastModifiedBy
    protected String lastModifiedBy;

    @CreatedDate
    protected Instant createdAt;

    @LastModifiedDate
    protected Instant lastModifiedAt;

}

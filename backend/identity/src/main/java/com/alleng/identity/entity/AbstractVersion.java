package com.alleng.identity.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractVersion extends Auditor {
    @Version
    private Long version;
}

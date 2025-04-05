package com.alleng.file.repository;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.file.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
    List<Outbox> findByStatus(OutboxStatus status);
}
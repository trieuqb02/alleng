package com.alleng.subscription.repository;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.subscription.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
    List<Outbox> findByStatus(OutboxStatus s);
}
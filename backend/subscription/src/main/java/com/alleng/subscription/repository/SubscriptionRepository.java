package com.alleng.subscription.repository;

import com.alleng.subscription.constant.CodeType;
import com.alleng.subscription.constant.StatusType;
import com.alleng.subscription.constant.SubscriptionType;
import com.alleng.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Query("""
    SELECT s FROM Subscription s
    JOIN s.plan.features f
    WHERE s.userId = ?1
      AND s.status = ?2
      AND f.code = ?3
      AND CURRENT_TIMESTAMP BETWEEN s.startDate AND s.endDate
    """)
    Optional<Subscription> findByUserIdAndStatusAndCode(UUID userId, SubscriptionType status, CodeType code);

}
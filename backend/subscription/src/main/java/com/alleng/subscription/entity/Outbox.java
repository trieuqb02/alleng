package com.alleng.subscription.entity;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.subscription.constant.EventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String aggregateType;

    private UUID aggregateId;

    @Column(columnDefinition = "json")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private Instant processedAt;

    private int retryCount = 0;
}

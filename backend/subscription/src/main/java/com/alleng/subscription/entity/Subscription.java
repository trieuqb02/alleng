package com.alleng.subscription.entity;

import com.alleng.subscription.constant.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Subscription extends AbstractVersion{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private SubscriptionType status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private UUID paymentId;

    @Column(length = 2048)
    private String paymentUrl;
}

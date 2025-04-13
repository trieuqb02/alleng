package com.alleng.subscription.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.subscription.constant.CodeType;
import com.alleng.subscription.constant.EventType;
import com.alleng.subscription.constant.SubscriptionType;
import com.alleng.subscription.entity.Outbox;
import com.alleng.subscription.entity.Plan;
import com.alleng.subscription.entity.Subscription;
import com.alleng.subscription.payload.request.SubscriptionVM;
import com.alleng.subscription.payload.response.SubscriptionMV;
import com.alleng.subscription.repository.OutboxRepository;
import com.alleng.subscription.repository.PlanRepository;
import com.alleng.subscription.repository.SubscriptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionService implements ISubscriptionService {

    SubscriptionRepository subscriptionRepository;

    PlanRepository planRepository;

    OutboxRepository outboxRepository;

    @Transactional
    @Override
    public SubscriptionMV createSubscription(SubscriptionVM vm, String subject) {

        Plan plan = planRepository.findById(vm.planId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PLAN_NOT_FOUND, vm.planId()));

        LocalDateTime startDate = LocalDate.now().atStartOfDay();

        int durationDate = plan.getDurationDay();

        LocalDateTime endDate = startDate.plusDays(durationDate - 1).with(LocalTime.MAX);

        Subscription subscription = Subscription.builder()
                .userId(UUID.fromString(subject))
                .status(SubscriptionType.PENDING)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        subscription = subscriptionRepository.save(subscription);

        String payload;
        try {
            payload = new ObjectMapper().writeValueAsString(Map.of(
                    "subscriptionId", subscription.getId(),
                    "userId", subject,
                    "price", plan.getPrice()
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Outbox outbox = Outbox.builder()
                .aggregateId(subscription.getId())
                .aggregateType("entity")
                .payload(payload)
                .eventType(EventType.PROCESSING_PAYMENT)
                .processedAt(Instant.now())
                .status(OutboxStatus.PENDING)
                .build();

        outboxRepository.save(outbox);

        return SubscriptionMV.convertToSubscriptionMV(subscription);
    }

    @Override
    public Boolean hasAccessToFeature(CodeType code, String subject) {
        return subscriptionRepository.findByUserIdAndStatusAndCode(UUID.fromString(subject), SubscriptionType.ACTIVE, code).isPresent();
    }
}

package com.alleng.subscription.payload.response;

import com.alleng.subscription.constant.SubscriptionType;
import com.alleng.subscription.entity.Subscription;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionMV(UUID subscriptionId, UUID userId, UUID planId, SubscriptionType statusType,
                             LocalDateTime startDate, LocalDateTime endDate, UUID paymentId) {

    public static SubscriptionMV convertToSubscriptionMV(Subscription subscription) {
        return new SubscriptionMV(subscription.getId(), subscription.getUserId(), subscription.getPlan().getId(), subscription.getStatus(), subscription.getStartDate(), subscription.getEndDate(), subscription.getPaymentId());
    }
}

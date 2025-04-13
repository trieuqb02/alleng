package com.alleng.payment.payload;

import java.math.BigDecimal;
import java.util.UUID;

public record SubscriptionConsumer(UUID subscriptionId, UUID userId, BigDecimal price) {
}

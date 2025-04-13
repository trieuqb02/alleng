package com.alleng.subscription.payload;

import java.io.Serializable;
import java.util.UUID;

public record PaymentConsumer(UUID subscriptionId, UUID paymentId, String paymentUrl) implements Serializable {
}

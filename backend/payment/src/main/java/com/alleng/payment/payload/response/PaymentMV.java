package com.alleng.payment.payload.response;

import com.alleng.payment.constant.PaymentMethod;
import com.alleng.payment.constant.PaymentStatus;
import com.alleng.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentMV(UUID id, UUID subscriptionId, UUID userId, BigDecimal amount, PaymentStatus paymentStatus,
                        PaymentMethod paymentMethod, LocalDateTime paidAt) {

    public static PaymentMV convertPaymentMV(Payment payment) {
        return new PaymentMV(payment.getId(), payment.getSubscriptionId(), payment.getUserId(), payment.getAmount(), payment.getPaymentStatus(), payment.getPaymentMethod(), payment.getPaidAt());
    }

}

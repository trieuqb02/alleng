package com.alleng.payment.service;

import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.payment.constant.PaymentStatus;
import com.alleng.payment.payload.SubscriptionConsumer;
import com.alleng.payment.payload.response.PaymentMV;

import java.util.UUID;

public interface IPaymentService {

    PaymentMV createPayment(SubscriptionConsumer consumer);

    PaymentMV updatePayment(UUID txnRef, PaymentStatus paymentStatus);

    PaginationMV<PaymentMV> getPaymentList(int page, int limit, String sortDir, String sortBy);
}

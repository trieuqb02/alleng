package com.alleng.payment.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.payment.constant.EventType;
import com.alleng.payment.constant.PaymentMethod;
import com.alleng.payment.constant.PaymentStatus;
import com.alleng.payment.entity.Outbox;
import com.alleng.payment.entity.Payment;
import com.alleng.payment.payload.SubscriptionConsumer;
import com.alleng.payment.payload.response.PaymentMV;
import com.alleng.payment.repository.OutboxRepository;
import com.alleng.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentService implements IPaymentService {

    PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;

    @Transactional
    @Override
    public PaymentMV createPayment(SubscriptionConsumer consumer) {
        Payment payment = Payment.builder()
                .userId(consumer.userId())
                .subscriptionId(consumer.subscriptionId())
                .amount(consumer.price())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(PaymentMethod.VNPAY)
                .paidAt(LocalDateTime.now())
                .build();

        return PaymentMV.convertPaymentMV(paymentRepository.save(payment));
    }

    @Override
    public PaymentMV updatePayment(UUID txnRef, PaymentStatus paymentStatus) {
        Payment payment = paymentRepository.findById(txnRef)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND, txnRef));

        payment.setPaymentStatus(paymentStatus);

        String payload;
        try {
            payload = new ObjectMapper().writeValueAsString(Map.of(
                    "subscriptionId", payment.getSubscriptionId(),
                    "paymentId", payment.getId(),
                    "status", paymentStatus
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Outbox outbox = new Outbox();
        outbox.setAggregateId(payment.getId());
        outbox.setStatus(OutboxStatus.PENDING);
        outbox.setAggregateType("entity");
        outbox.setProcessedAt(Instant.now());
        outbox.setPayload(payload);

        if (PaymentStatus.SUCCESS.equals(paymentStatus)) {
            outbox.setEventType(EventType.PAYMENT_SUCCESS);
        } else if (PaymentStatus.CANCEL.equals(paymentStatus)) {
            outbox.setEventType(EventType.PAYMENT_CANCEL);
        } else if (PaymentStatus.FAILED.equals(paymentStatus)) {
            outbox.setEventType(EventType.PAYMENT_FAILED);
        }

        outboxRepository.save(outbox);

        return PaymentMV.convertPaymentMV(paymentRepository.save(payment));
    }

    @Override
    public PaginationMV<PaymentMV> getPaymentList(int page, int limit, String sortDir, String sortBy) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Page<Payment> resultPage = paymentRepository.findAll(pageable);

        List<PaymentMV> collect = resultPage.getContent().stream().map(PaymentMV::convertPaymentMV).collect(Collectors.toList());

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }
}

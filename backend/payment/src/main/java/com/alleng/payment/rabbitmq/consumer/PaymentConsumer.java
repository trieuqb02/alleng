package com.alleng.payment.rabbitmq.consumer;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.payment.constant.EventType;
import com.alleng.payment.entity.Outbox;
import com.alleng.payment.payload.SubscriptionConsumer;
import com.alleng.payment.payload.response.PaymentMV;
import com.alleng.payment.repository.OutboxRepository;
import com.alleng.payment.service.IPaymentService;
import com.alleng.payment.service.IVNPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentConsumer {

    PasserUtil passerUtil;

    IVNPayService vnPayService;

    IPaymentService paymentService;

    OutboxRepository outboxRepository;

    ServletWebServerApplicationContext webServerAppCtxt;

    @Transactional
    @RabbitListener(queues = RabbitMQConstant.QUEUE_SUBSCRIPTION)
    public void receivePaymentMessage(String payload) {
        SubscriptionConsumer subscriptionConsumer = passerUtil.parseToObject(payload, SubscriptionConsumer.class);

        PaymentMV paymentMV = paymentService.createPayment(subscriptionConsumer);

        String hostAddress = "localhost";

        int port = webServerAppCtxt.getWebServer().getPort();

        String returnUrl = "http://" + hostAddress + ":" + port + "/api/v1/payment";

        String vnpayUrl = vnPayService.vnpayUrl(paymentMV.id(), paymentMV.amount(), returnUrl);

        String payl;
        try {
            payl = new ObjectMapper().writeValueAsString(Map.of(
                    "subscriptionId", paymentMV.subscriptionId(),
                    "paymentId", paymentMV.id(),
                    "paymentUrl", vnpayUrl
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Outbox outbox = Outbox.builder()
                .eventType(EventType.PAYMENT_VNPAY)
                .aggregateId(paymentMV.id())
                .aggregateType("entity")
                .processedAt(Instant.now())
                .status(OutboxStatus.PENDING)
                .payload(payl)
                .build();

        outboxRepository.save(outbox);
    }
}

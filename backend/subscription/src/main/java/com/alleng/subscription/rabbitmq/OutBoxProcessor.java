package com.alleng.subscription.rabbitmq;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.subscription.constant.EventType;
import com.alleng.subscription.entity.Outbox;
import com.alleng.subscription.rabbitmq.producer.PaymentProducer;
import com.alleng.subscription.repository.OutboxRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OutBoxProcessor {

    OutboxRepository outboxRepository;

    PaymentProducer paymentProducer;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void outBoxProcessor() {
        List<Outbox> outboxes = outboxRepository.findByStatus(OutboxStatus.PENDING);
        outboxes.forEach(outbox -> {
            try {
                String payload = outbox.getPayload();
                switch (outbox.getEventType()) {
                    case EventType.PROCESSING_PAYMENT ->
                            paymentProducer.senPaymentMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_DIRECT_SUBSCRIPTION, payload);

                }
                outbox.setStatus(OutboxStatus.SENT);
                outboxRepository.save(outbox);
            } catch (IOException e) {
                throw new RuntimeException("error message");
            }
        });
    }
}

package com.alleng.payment.rabbitmq;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.payment.constant.EventType;
import com.alleng.payment.entity.Outbox;
import com.alleng.payment.rabbitmq.producer.SubscriptionProducer;
import com.alleng.payment.repository.OutboxRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OutBoxProcessor {

    OutboxRepository outboxRepository;

    SubscriptionProducer subscriptionProducer;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void outBoxProcessor() {
        System.out.println(1);
        List<Outbox> outboxes = outboxRepository.findByStatus(OutboxStatus.PENDING);
        outboxes.forEach(outbox -> {
            String payload = outbox.getPayload();
            switch (outbox.getEventType()) {
                case EventType.PAYMENT_SUCCESS ->
                        subscriptionProducer.sendPaymentSuccessMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_DIRECT_PAYMENT_SUCCESS, payload);
                case EventType.PAYMENT_CANCEL ->
                        subscriptionProducer.sendPaymentCancelMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_DIRECT_PAYMENT_CANCEL, payload);
                case EventType.PAYMENT_FAILED ->
                        subscriptionProducer.sendPaymentFailedMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_DIRECT_FILE_FAILED, payload);
                case EventType.PAYMENT_VNPAY ->
                        subscriptionProducer.sendPaymentMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_DIRECT_VNPAY_URL, payload);
            }
            outbox.setStatus(OutboxStatus.SENT);
            outboxRepository.save(outbox);
        });
    }
}

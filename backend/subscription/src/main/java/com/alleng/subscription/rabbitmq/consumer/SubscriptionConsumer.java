package com.alleng.subscription.rabbitmq.consumer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.subscription.constant.SubscriptionType;
import com.alleng.subscription.entity.Subscription;
import com.alleng.subscription.payload.PaymentConsumer;
import com.alleng.subscription.repository.SubscriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionConsumer {

    SubscriptionRepository subscriptionRepository;

    PasserUtil passerUtil;

    @Transactional
    @RabbitListener(queues = RabbitMQConstant.QUEUE_VNPAY_URL)
    public void handleVNPayURL(String payload) {
        PaymentConsumer paymentConsumer = passerUtil.parseToObject(payload, PaymentConsumer.class);
        Optional<Subscription> subscription = subscriptionRepository.findById(paymentConsumer.subscriptionId());
        if (subscription.isPresent()) {
            subscription.get().setPaymentId(paymentConsumer.paymentId());
            subscription.get().setPaymentUrl(paymentConsumer.paymentUrl());
            subscriptionRepository.save(subscription.get());
        }
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConstant.QUEUE_PAYMENT_SUCCESS)
    public void handlePaymentSuccess(String payload) {
        System.out.println(2);
        PaymentConsumer paymentConsumer = passerUtil.parseToObject(payload, PaymentConsumer.class);
        Optional<Subscription> subscription = subscriptionRepository.findById(paymentConsumer.subscriptionId());
        if (subscription.isPresent()) {
            subscription.get().setStatus(SubscriptionType.ACTIVE);
            subscriptionRepository.save(subscription.get());
        }
        // send message to user
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConstant.QUEUE_PAYMENT_FAILED)
    public void handlePaymentFailed(String payload) {
        PaymentConsumer paymentConsumer = passerUtil.parseToObject(payload, PaymentConsumer.class);
        Optional<Subscription> subscription = subscriptionRepository.findById(paymentConsumer.subscriptionId());
        if (subscription.isPresent()) {
            subscription.get().setStatus(SubscriptionType.FAILED);
            subscriptionRepository.save(subscription.get());
        }
        // send message to user
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConstant.QUEUE_PAYMENT_CANCEL)
    public void handlePaymentCancel(String payload) {
        PaymentConsumer paymentConsumer = passerUtil.parseToObject(payload, PaymentConsumer.class);
        Optional<Subscription> subscription = subscriptionRepository.findById(paymentConsumer.subscriptionId());
        if (subscription.isPresent()) {
            subscription.get().setStatus(SubscriptionType.CANCELED);
            subscriptionRepository.save(subscription.get());
        }
        // send message to user
    }
}

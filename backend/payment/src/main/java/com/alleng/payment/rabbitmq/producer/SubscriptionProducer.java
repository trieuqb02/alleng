package com.alleng.payment.rabbitmq.producer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionProducer {

    RabbitTemplate rabbitTemplate;

    public void sendPaymentFailedMessage(String s, String routingKey, String payload) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_PAYMENT, routingKey, payload);
    }

    public void sendPaymentCancelMessage(String s, String routingKey, String payload) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_PAYMENT, routingKey, payload);
    }

    public void sendPaymentSuccessMessage(String s, String routingKey, String payload) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_PAYMENT, routingKey, payload);
    }

    public void sendPaymentMessage(String s, String routingKey, String payload) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_PAYMENT, routingKey, payload);
    }
}

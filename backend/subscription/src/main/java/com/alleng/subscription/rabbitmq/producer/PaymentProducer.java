package com.alleng.subscription.rabbitmq.producer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentProducer {

    RabbitTemplate rabbitTemplate;

    PasserUtil passerUtil;

    public void senPaymentMessage(String messageId, String routingKey, String payload) throws IOException {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_SUBSCRIPTION, routingKey, payload);
    }
}

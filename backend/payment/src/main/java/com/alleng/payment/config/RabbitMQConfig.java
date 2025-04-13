package com.alleng.payment.config;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_DIRECT_PAYMENT);
    }

    @Bean
    public Binding subscriptionSuccessBinding() {
        return BindingBuilder.bind(subscriptionSuccessQueue()).to(paymentExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_PAYMENT_SUCCESS);
    }

    @Bean
    public Binding subscriptionFaliedBinding() {
        return BindingBuilder.bind(subscriptionFailedQueue()).to(paymentExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_PAYMENT_FAILED);
    }

    @Bean
    public Binding subscriptionCancelBinding() {
        return BindingBuilder.bind(subscriptionCancelQueue()).to(paymentExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_PAYMENT_CANCEL);
    }

    @Bean
    public Binding vnpayBinding() {
        return BindingBuilder.bind(vnpayQueue()).to(paymentExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_VNPAY_URL);
    }

    @Bean
    public Queue subscriptionSuccessQueue() {
        return new Queue(RabbitMQConstant.QUEUE_PAYMENT_SUCCESS, true, false, false);
    }

    @Bean
    public Queue subscriptionFailedQueue() {
        return new Queue(RabbitMQConstant.QUEUE_PAYMENT_FAILED, true, false, false);
    }

    @Bean
    public Queue subscriptionCancelQueue() {
        return new Queue(RabbitMQConstant.QUEUE_PAYMENT_CANCEL, true, false, false);
    }

    @Bean
    public Queue vnpayQueue() {
        return new Queue(RabbitMQConstant.QUEUE_VNPAY_URL, true, false, false);
    }
}

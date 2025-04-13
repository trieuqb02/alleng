package com.alleng.subscription.config;

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
    public DirectExchange subscriptionExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_DIRECT_SUBSCRIPTION);
    }

    @Bean
    public Binding subscriptionBinding() {
        return BindingBuilder.bind(subscriptionQueue()).to(subscriptionExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_SUBSCRIPTION);
    }

    @Bean
    public Queue subscriptionQueue() {
        return new Queue(RabbitMQConstant.QUEUE_SUBSCRIPTION, true, false, false);
    }
}

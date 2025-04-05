package com.alleng.file.config;

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
    public DirectExchange newsDirectExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_DIRECT_NEWS);
    }

    @Bean
    public Queue newsCompletedQueue() {
        return new Queue(RabbitMQConstant.QUEUE_NEWS_COMPLETED, true, false, false);
    }

    @Bean
    public Queue newsFailedQueue() {
        return new Queue(RabbitMQConstant.QUEUE_NEWS_FAILED, true, false, false);
    }

    @Bean
    public Binding newsCompletedBinding() {
        return BindingBuilder.bind(newsCompletedQueue()).to(newsDirectExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_FILE_COMPLETED);
    }

    @Bean
    public Binding newsFailedbinding() {
        return BindingBuilder.bind(newsFailedQueue()).to(newsDirectExchange()).with(RabbitMQConstant.ROUTING_KEY_DIRECT_FILE_FAILED);
    }
}

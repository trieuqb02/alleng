package com.alleng.news.config;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange newsChange() {
        return new TopicExchange(RabbitMQConstant.EXCHANGE_TOPIC_NEWS);
    }

    @Bean
    public Queue newsQueue() {
        return new Queue(RabbitMQConstant.QUEUE_FILE, true, false, false);
    }

    @Bean
    public Binding newsBinding() {
        return BindingBuilder.bind(newsQueue()).to(newsChange()).with(RabbitMQConstant.ROUTING_KEY_TOPIC_FILE);
    }
}

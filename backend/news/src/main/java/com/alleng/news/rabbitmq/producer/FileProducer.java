package com.alleng.news.rabbitmq.producer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.news.payload.MediaPayload;
import com.alleng.news.payload.MediaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileProducer {

    RabbitTemplate rabbitTemplate;

    PasserUtil passerUtil;

    public void sendFileMessage(String messageId, String routingKey, String payload) throws IOException {
        MediaPayload mediaPayload = passerUtil.parseToObject(payload, MediaPayload.class);
        MediaProducer mediaProducer = new MediaProducer(mediaPayload.newsId(), mediaPayload.image(), mediaPayload.audio(), mediaPayload.imageName(), mediaPayload.audioName());
        byte[] messageBody = new ObjectMapper().writeValueAsBytes(mediaProducer);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/octet-stream");
        messageProperties.setMessageId(messageId);
        messageProperties.setHeader("Authorization", mediaPayload.token());
        Message message = new Message(messageBody, messageProperties);
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_TOPIC_NEWS, routingKey, message);
    }
}

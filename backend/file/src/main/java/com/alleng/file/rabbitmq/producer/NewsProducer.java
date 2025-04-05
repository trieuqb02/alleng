package com.alleng.file.rabbitmq.producer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.file.payload.MediaPayload;
import com.alleng.file.payload.MediaProducer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NewsProducer {

    RabbitTemplate rabbitTemplate;

    PasserUtil passerUtil;

    public void sendFilesPathMessage(String payload) {
        MediaPayload mediaPayloadConsumer = passerUtil.parseToObject(payload, MediaPayload.class);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("Authorization", mediaPayloadConsumer.token());
        MediaProducer mediaProducer = new MediaProducer(mediaPayloadConsumer.newsId(), mediaPayloadConsumer.image(), mediaPayloadConsumer.audio());
        String json = passerUtil.parseToJson(mediaProducer);
        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_NEWS, RabbitMQConstant.ROUTING_KEY_DIRECT_FILE_COMPLETED, message);
    }

    public void sendUploadFailureMessage(String payload) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT_NEWS, RabbitMQConstant.ROUTING_KEY_DIRECT_FILE_FAILED, payload);
    }
}

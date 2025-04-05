package com.alleng.file.rabbitmq.consumer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.file.payload.MediaConsumer;
import com.alleng.file.repository.IdempotentRepository;
import com.alleng.file.service.IFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileConsumer {

    PasserUtil passerUtil;

    IFileService fileService;

    IdempotentRepository idempotentRepository;

    @RabbitListener(queues = RabbitMQConstant.QUEUE_FILE)
    public void receiveFileMessage(Message message) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        String messageId = messageProperties.getMessageId();
        String authorizationHeader = messageProperties.getHeader("Authorization");
//        boolean check = idempotentRepository.existsById(messageId);
//        if (check) {
//            return;
//        }
//        idempotentRepository.save(new Idempotent(messageId));

        String routingKey = messageProperties.getReceivedRoutingKey();
        MediaConsumer mediaConsumer = passerUtil.parseToObject(message.getBody(), MediaConsumer.class);
        switch (routingKey) {
            case RabbitMQConstant.ROUTING_KEY_TOPIC_FILE_CREATE ->
                    fileService.createFile(mediaConsumer, authorizationHeader);
            case RabbitMQConstant.ROUTING_KEY_TOPIC_FILE_UPDATE ->
                    fileService.updateFile(mediaConsumer, authorizationHeader);
            case RabbitMQConstant.ROUTING_KEY_TOPIC_FILE_DELETE -> fileService.deleteFile(mediaConsumer);
        }
    }
}

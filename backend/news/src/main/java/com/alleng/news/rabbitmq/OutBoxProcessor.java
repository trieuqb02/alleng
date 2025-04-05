package com.alleng.news.rabbitmq;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.news.constant.EventType;
import com.alleng.news.entity.Outbox;
import com.alleng.news.rabbitmq.producer.FileProducer;
import com.alleng.news.repository.OutboxRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OutBoxProcessor {
    FileProducer fileProducer;

    PasserUtil passerUtil;

    OutboxRepository outboxRepository;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void outBoxProcessor() {
        List<Outbox> outboxes = outboxRepository.findByStatus(OutboxStatus.PENDING);
        outboxes.forEach(outbox -> {
            try {
                String payload = outbox.getPayload();
                switch (outbox.getEventType()) {
                    case EventType.SAVE_FILE ->
                            fileProducer.sendFileMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_TOPIC_FILE_CREATE, payload);
                    case EventType.UPDATE_FILE ->
                            fileProducer.sendFileMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_TOPIC_FILE_UPDATE, payload);
                    case EventType.DELETE_FILE ->
                            fileProducer.sendFileMessage(String.valueOf(outbox.getId()), RabbitMQConstant.ROUTING_KEY_TOPIC_FILE_DELETE, payload);
                }
                outbox.setStatus(OutboxStatus.SENT);
                outboxRepository.save(outbox);
            } catch (IOException e) {
                throw new RuntimeException("error message");
            }
        });
    }
}

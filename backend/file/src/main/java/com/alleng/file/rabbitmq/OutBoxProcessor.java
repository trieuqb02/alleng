package com.alleng.file.rabbitmq;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.file.constant.EventType;
import com.alleng.file.entity.Outbox;
import com.alleng.file.rabbitmq.producer.NewsProducer;
import com.alleng.file.repository.OutboxRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OutBoxProcessor {

    OutboxRepository outboxRepository;

    NewsProducer newsProducer;

    @Scheduled(fixedRate = 5000)
    public void outBoxProcessor() {
        List<Outbox> outboxes = outboxRepository.findByStatus(OutboxStatus.PENDING);

        outboxes.forEach(outbox -> {
            String payload = outbox.getPayload();
            switch (outbox.getEventType()) {
                case EventType.COMPLETED -> {
                    newsProducer.sendFilesPathMessage(payload);
                }
                case EventType.FAILED -> {
                    newsProducer.sendUploadFailureMessage(payload);
                }
            }
            outbox.setStatus(OutboxStatus.SENT);
            outboxRepository.save(outbox);
        });
    }
}

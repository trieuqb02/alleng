package com.alleng.file.service;

import com.alleng.commonlibrary.constant.OutboxStatus;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.file.constant.EventType;
import com.alleng.file.entity.Outbox;
import com.alleng.file.payload.MediaConsumer;
import com.alleng.file.payload.MediaPayload;
import com.alleng.file.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService implements IFileService {

    @Value("${spring.resources.static-locations}")
    private String path;

    private final OutboxRepository outboxRepository;

    private final PasserUtil passerUtil;

    @Transactional
    @Override
    public void createFile(MediaConsumer mediaConsumer, String authorizationHeader) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        String imageName = !mediaConsumer.image().isEmpty() ? UUID.randomUUID() + ".png" : "";
        String audioName = !mediaConsumer.audio().isEmpty() ? UUID.randomUUID() + ".mp3" : "";
        Path imagePath = !imageName.isEmpty() ? Path.of(path, imageName) : null;
        Path audioPath = !audioName.isEmpty() ? Path.of(path, audioName) : null;

        try {
            if (imagePath != null) {
                Files.write(imagePath, passerUtil.convertBase64ToBytes(mediaConsumer.image()));
            }

            if (audioPath != null) {
                Files.write(audioPath, passerUtil.convertBase64ToBytes(mediaConsumer.audio()));
            }

            String url = "http://localhost:9000/api/v1/file";
            String imageURL = imagePath != null ? url + "/image/" + imageName : "";
            String audioURL = audioPath != null ? url + "/audio/" + audioName : "";
            MediaPayload mediaPayload = new MediaPayload(authorizationHeader, mediaConsumer.newsId(), imageURL, audioURL);
            String payload = passerUtil.parseToJson(mediaPayload);

            Outbox outbox = Outbox.builder()
                    .payload(payload)
                    .processedAt(Instant.now())
                    .eventType(EventType.COMPLETED)
                    .status(OutboxStatus.PENDING).build();

            outboxRepository.save(outbox);
        } catch (IOException e) {
            String image = mediaConsumer.imageName() != null ? mediaConsumer.imageName() : "";
            String audio = mediaConsumer.audioName() != null ? mediaConsumer.audioName() : "";
            MediaPayload mediaPayload = new MediaPayload("", mediaConsumer.newsId(), image, audio);
            String payload = passerUtil.parseToJson(mediaPayload);

            Outbox outbox = Outbox.builder()
                    .payload(payload)
                    .processedAt(Instant.now())
                    .eventType(EventType.FAILED)
                    .status(OutboxStatus.PENDING)
                    .build();

            outboxRepository.save(outbox);
        }

    }

    @Override
    public void updateFile(MediaConsumer mediaConsumer, String authorizationHeader) {
        createFile(mediaConsumer, authorizationHeader);
        deleteFile(mediaConsumer);
    }

    @Override
    public void deleteFile(MediaConsumer mediaConsumer) {
        boolean isDeleteImage = false;
        boolean isDeleteAudio = false;
        try {
            if (!mediaConsumer.audioName().isEmpty()) {
                Path audioFile = Paths.get(path, mediaConsumer.audioName());
                if (Files.exists(audioFile)) {
                    Files.delete(audioFile);
                    isDeleteAudio = true;
                }
            }

            if (!mediaConsumer.imageName().isEmpty()) {
                Path imageFile = Paths.get(path, mediaConsumer.imageName());
                if (Files.exists(imageFile)) {
                    Files.delete(imageFile);
                    isDeleteImage = true;
                }
            }
        } catch (IOException e) {
            String image = mediaConsumer.imageName() != null ? mediaConsumer.imageName() : "";
            String audio = mediaConsumer.audioName() != null ? mediaConsumer.audioName() : "";
            MediaPayload mediaPayload = new MediaPayload("", mediaConsumer.newsId(), image, audio);
            String payload = passerUtil.parseToJson(mediaPayload);
            Outbox outbox = Outbox.builder()
                    .payload(payload)
                    .processedAt(Instant.now())
                    .eventType(EventType.FAILED)
                    .status(OutboxStatus.PENDING)
                    .build();
            outboxRepository.save(outbox);
        }
    }
}

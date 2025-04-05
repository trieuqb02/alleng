package com.alleng.news.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.BadRequestException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.news.entity.Topic;
import com.alleng.news.payload.request.TopicVM;
import com.alleng.news.payload.response.TopicMV;
import com.alleng.news.repository.TopicRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TopicService implements ITopicService {

    TopicRepository topicRepository;

    @Transactional(readOnly = true)
    @Override
    public List<TopicMV> getAllTopic() {
        List<Object[]> results = topicRepository.findTopicsWithNewsCount();
        return results.stream()
                .map(r -> new TopicMV(
                        (UUID) r[0],
                        (String) r[1],
                        (String) r[2],
                        (Boolean) r[3],
                        (int) ((Number) r[4]).longValue()))
                .toList();
    }

    @Transactional
    @Override
    public TopicMV createTopic(TopicVM topicVM) {
        boolean checkName = topicRepository.existsByName(topicVM.name());
        if (checkName) {
            throw new BadRequestException(ErrorCode.TOPIC_NAME_EXIST, topicVM.name());
        }
        Topic topic = Topic.builder()
                .name(topicVM.name())
                .description(topicVM.description())
                .enable(topicVM.enable())
                .build();

        return TopicMV.convertTopicVM(topicRepository.save(topic));
    }

    @Transactional
    @Override
    public TopicMV updateTopic(UUID topicId, TopicVM topicVM) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOPIC_NOT_FOUND, topicId));

        if (!topic.getName().equals(topicVM.name())) {
            boolean checkName = topicRepository.existsByName(topicVM.name());
            if (checkName) {
                throw new BadRequestException(ErrorCode.TOPIC_NAME_EXIST, topicVM.name());
            }
        }
        topic.setName(topicVM.name());
        topic.setDescription(topicVM.description());
        topic.setEnable(topicVM.enable());
        topic = topicRepository.save(topic);
        return TopicMV.convertTopicVM(topic);
    }

    @Transactional
    @Override
    public List<UUID> deleteTopic(List<UUID> topicIdList) {
        List<Topic> topics = topicRepository.findAllById(topicIdList);

        List<Topic> topicsToUpdate = new ArrayList<>();
        List<UUID> topicIdsToDelete = new ArrayList<>();

        topics.forEach(topic -> {
            if (!topic.getNews().isEmpty()) {
                topic.setEnable(false);
                topicsToUpdate.add(topic);
            } else {
                topicIdsToDelete.add(topic.getId());
            }
        });

        if (!topicsToUpdate.isEmpty()) {
            topicRepository.saveAll(topicsToUpdate);
        }

        if (!topicIdsToDelete.isEmpty()) {
            topicRepository.deleteAllById(topicIdsToDelete);
        }

        return topicIdsToDelete;
    }
}

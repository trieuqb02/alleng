package com.alleng.news.service;

import com.alleng.news.payload.request.TopicVM;
import com.alleng.news.payload.response.TopicMV;

import java.util.List;
import java.util.UUID;

public interface ITopicService {

    List<TopicMV> getAllTopic();

    TopicMV createTopic(TopicVM topicVM);

    TopicMV updateTopic(UUID topicId, TopicVM topicVM);

    List<UUID> deleteTopic(List<UUID> topicIdList);
}

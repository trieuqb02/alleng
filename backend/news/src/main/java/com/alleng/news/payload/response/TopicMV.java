package com.alleng.news.payload.response;

import com.alleng.news.entity.Topic;

import java.util.UUID;

public record TopicMV(UUID id, String name, String description, boolean enable, int quantityNews) {

    public static TopicMV convertTopicVM(Topic topic) {
        return new TopicMV(topic.getId(), topic.getName(), topic.getDescription(), topic.isEnable(), 0);
    }
}

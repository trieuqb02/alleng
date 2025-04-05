package com.alleng.news.payload.response;

import com.alleng.news.entity.News;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record NewsMV(UUID uuid, String title, String topicName, String sourceName, String thumbnail, String audio,
                     List<ParagraphMV> paragraphList) {

    public static NewsMV convertNewsMV(News news) {
        List<ParagraphMV> list = news.getParagraphs().stream()
                .map(ParagraphMV::convertParagraphMV).collect(Collectors.toList());
        return new NewsMV(news.getId(), news.getTitle(), news.getTopic().getName(), news.getSource().getName(), news.getThumbnail(), news.getAudio(), list);
    }
}

package com.alleng.news.payload.response;

import com.alleng.news.entity.Source;

import java.util.UUID;

public record SourceMV(UUID id, String name, String description, boolean enable, int quantityNews) {
    public static SourceMV convertSourceMV(Source source) {
        return new SourceMV(source.getId(), source.getName(), source.getDescription(), source.isEnable(), 0);
    }
}

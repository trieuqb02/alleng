package com.alleng.news.payload.response;

import com.alleng.news.entity.Paragraph;

import java.util.UUID;

public record ParagraphMV(UUID id, String content) {
    public static ParagraphMV convertParagraphMV(Paragraph paragraph){
        return new ParagraphMV(paragraph.getId(), paragraph.getContent());
    }
}

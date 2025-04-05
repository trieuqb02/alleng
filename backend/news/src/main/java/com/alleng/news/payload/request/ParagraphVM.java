package com.alleng.news.payload.request;

import com.alleng.news.entity.News;
import com.alleng.news.entity.Paragraph;

import java.util.UUID;

public record ParagraphVM(UUID id, String content) {
    public static Paragraph convertParagraph(ParagraphVM paragraphVM, News news) {
        return new Paragraph(paragraphVM.id, paragraphVM.content(), news);
    }
}

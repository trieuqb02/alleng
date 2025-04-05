package com.alleng.news.payload.request;

import com.alleng.news.constant.StatusType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record NewsVM(String title, StatusType status, boolean enable, Date publicationDate, UUID topicId, UUID sourceId,
                     List<ParagraphVM> paragraphs) {
}

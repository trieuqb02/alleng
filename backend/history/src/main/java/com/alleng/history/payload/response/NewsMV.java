package com.alleng.history.payload.response;

import com.alleng.history.payload.response.ParagraphMV;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record NewsMV(UUID uuid, String title, String topicName, String sourceName, String thumbnail, String audio,
                     List<ParagraphMV> paragraphList) implements Serializable {

}

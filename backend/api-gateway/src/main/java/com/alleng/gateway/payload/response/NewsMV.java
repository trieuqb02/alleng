package com.alleng.gateway.payload.response;

import java.util.List;
import java.util.UUID;

public record NewsMV(UUID uuid, String title, String topicName, String sourceName, String thumbnail, String audio,
                     List<ParagraphMV> paragraphList) {

}

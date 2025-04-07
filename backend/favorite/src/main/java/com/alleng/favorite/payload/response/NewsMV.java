package com.alleng.favorite.payload.response;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record NewsMV(UUID uuid, String title, String topicName, String sourceName, String thumbnail, String audio,
                     List<ParagraphMV> paragraphList) implements Serializable {

}

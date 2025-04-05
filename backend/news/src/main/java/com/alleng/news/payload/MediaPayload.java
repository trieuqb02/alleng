package com.alleng.news.payload;

import java.io.Serializable;

public record MediaPayload(String token, String newsId, String image, String audio, String imageName,
                           String audioName) implements Serializable {
}

package com.alleng.file.payload;

import java.io.Serializable;

public record MediaConsumer(String newsId, String image, String audio, String imageName,
                            String audioName) implements Serializable {
}

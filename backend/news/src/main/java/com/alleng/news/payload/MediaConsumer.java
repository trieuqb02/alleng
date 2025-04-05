package com.alleng.news.payload;

import java.io.Serializable;

public record MediaConsumer(String newsId, String imagePath, String audioPath) implements Serializable {
}

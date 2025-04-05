package com.alleng.file.payload;

import java.io.Serializable;

public record MediaProducer(String newsId, String imagePath, String audioPath) implements Serializable {
}

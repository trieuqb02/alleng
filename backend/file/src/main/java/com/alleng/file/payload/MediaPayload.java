package com.alleng.file.payload;

import java.io.Serializable;

public record MediaPayload(String token, String newsId, String image, String audio) implements Serializable {
}

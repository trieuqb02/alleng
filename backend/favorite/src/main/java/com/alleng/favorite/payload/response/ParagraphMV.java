package com.alleng.favorite.payload.response;

import java.io.Serializable;
import java.util.UUID;

public record ParagraphMV(UUID id, String content) implements Serializable {

}

package com.alleng.comment.payload.response;

import java.util.UUID;

public record UserMV(UUID userId, String fullName, String thumbnail) {
}

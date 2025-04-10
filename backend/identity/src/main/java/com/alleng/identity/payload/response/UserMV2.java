package com.alleng.identity.payload.response;

import java.util.UUID;

public record UserMV2(UUID userId, String fullName, String thumbnail) {
}

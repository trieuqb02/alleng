package com.alleng.history.payload.response;

import java.util.UUID;

public record HistoryMV(UUID uuid, UUID newsId, UUID userId) {
}

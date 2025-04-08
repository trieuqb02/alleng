package com.alleng.history.payload.response;

import java.util.UUID;

public record HistoryWithNewsMV(UUID uuid, NewsMV news) {
}

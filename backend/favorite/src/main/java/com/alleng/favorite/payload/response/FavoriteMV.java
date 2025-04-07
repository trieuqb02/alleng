package com.alleng.favorite.payload.response;

import java.util.UUID;

public record FavoriteMV(UUID uuid, UUID newsId, String username) {
}

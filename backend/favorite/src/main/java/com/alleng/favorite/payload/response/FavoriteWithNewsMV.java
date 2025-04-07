package com.alleng.favorite.payload.response;

import java.util.UUID;

public record FavoriteWithNewsMV(UUID uuid, NewsMV news) {
}

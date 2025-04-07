package com.alleng.favorite.service;

import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.favorite.payload.request.PaginationVM;
import com.alleng.favorite.payload.response.FavoriteMV;
import com.alleng.favorite.payload.response.FavoriteWithNewsMV;

import java.util.UUID;

public interface IFavoriteService {
    FavoriteMV addNews(UUID newsId, String subject);

    void deleteFavorite(UUID favoriteId, String subject);

    PaginationMV<FavoriteWithNewsMV> getFavorites(PaginationVM paginationVM, String subject);
}

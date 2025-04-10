package com.alleng.favorite.service;

import com.alleng.commonlibrary.constant.ErrorCode;
import com.alleng.commonlibrary.exception.AccessDeniedException;
import com.alleng.commonlibrary.exception.NotFoundException;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.favorite.entity.Favorite;
import com.alleng.favorite.feign.NewsClient;
import com.alleng.favorite.payload.request.PaginationVM;
import com.alleng.favorite.payload.response.FavoriteMV;
import com.alleng.favorite.payload.response.FavoriteWithNewsMV;
import com.alleng.favorite.payload.response.NewsMV;
import com.alleng.favorite.repository.FavoriteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FavoriteService implements IFavoriteService {

    FavoriteRepository favoriteRepository;

    NewsClient newsClient;

    @Override
    public FavoriteMV addNews(UUID newsId, String subject) {
        Favorite favorite = Favorite.builder()
                .newsId(newsId)
                .userId(UUID.fromString(subject))
                .favoriteAt(new Date())
                .build();

        favorite = favoriteRepository.save(favorite);
        return new FavoriteMV(favorite.getId(), favorite.getNewsId(), favorite.getUserId());
    }

    @Override
    public void deleteFavorite(UUID favoriteId, String subject) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FAVORITE_NOT_FOUND));
        if (!favorite.getUserId().equals(UUID.fromString(subject))) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENICE);
        }
        favoriteRepository.delete(favorite);
    }

    @Override
    public PaginationMV<FavoriteWithNewsMV> getFavorites(PaginationVM paginationVM, String subject) {
        Sort sort = paginationVM.sortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(paginationVM.sortBy()).ascending() : Sort.by(paginationVM.sortBy()).descending();

        Pageable pageable = PageRequest.of(paginationVM.page() - 1, paginationVM.limit(), sort);

        Page<Favorite> resultPage = favoriteRepository.findAllByUserId(UUID.fromString(subject), pageable);

        List<Favorite> favorites = resultPage.getContent();

        List<UUID> ids = favorites.stream().map(Favorite::getNewsId).toList();

        List<NewsMV> newsMVList = Objects.requireNonNull(newsClient.getListFavorite(ids).getBody()).data();

        Map<UUID, NewsMV> newsMap = newsMVList.stream()
                .collect(Collectors.toMap(NewsMV::uuid, Function.identity()));

        List<FavoriteWithNewsMV> collect = favorites.stream()
                .map(fav -> {
                    NewsMV news = newsMap.get(fav.getNewsId());
                    return new FavoriteWithNewsMV(fav.getId(), news);
                })
                .toList();

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }
}

package com.alleng.favorite.controller;

import com.alleng.commonlibrary.constant.PaginationConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.favorite.constant.CurrentUser;
import com.alleng.favorite.payload.request.PaginationVM;
import com.alleng.favorite.payload.response.FavoriteMV;
import com.alleng.favorite.payload.response.FavoriteWithNewsMV;
import com.alleng.favorite.service.IFavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${com.alleng.prefix.api:/api/v1}/favorite/")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FavoriteController {

    IFavoriteService favoriteService;

    @PostMapping("news/{newsId}")
    public ResponseEntity<ApiVM<FavoriteMV>> addNews(@PathVariable("newsId") UUID newsId, @CurrentUser Jwt jwt) {
        FavoriteMV favoriteMV = favoriteService.addNews(newsId, jwt.getSubject());
        ApiVM<FavoriteMV> apiVM = new ApiVM<>(favoriteMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @GetMapping("")
    public ResponseEntity<ApiVM<PaginationMV<FavoriteWithNewsMV>>> saveFavouriteNewspaper(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_ID) String sortBy,
            @CurrentUser Jwt jwt) {
        PaginationVM paginationVM = new PaginationVM(page, limit, sortDir, sortBy);
        PaginationMV<FavoriteWithNewsMV> paginationMV = favoriteService.getFavorites(paginationVM, jwt.getSubject());
        ApiVM<PaginationMV<FavoriteWithNewsMV>> apiVM = new ApiVM<>(paginationMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @DeleteMapping("{favoriteId}")
    public ResponseEntity<ApiVM<UUID>> deleteNews(@PathVariable("favoriteId") UUID favoriteId, @CurrentUser Jwt jwt) {
        favoriteService.deleteFavorite(favoriteId, jwt.getSubject());
        ApiVM<UUID> apiVM = new ApiVM<>(favoriteId);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}

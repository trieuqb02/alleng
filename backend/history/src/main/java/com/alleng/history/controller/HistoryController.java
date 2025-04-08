package com.alleng.history.controller;

import com.alleng.commonlibrary.constant.PaginationConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.history.constant.CurrentUser;
import com.alleng.history.payload.request.PaginationVM;
import com.alleng.history.payload.response.HistoryMV;
import com.alleng.history.payload.response.HistoryWithNewsMV;
import com.alleng.history.service.IHistoryService;
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
@RequestMapping("${com.alleng.prefix.api:/api/v1}/history/")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HistoryController {

    IHistoryService historyService;

    @PostMapping("news/{newsId}")
    public ResponseEntity<ApiVM<HistoryMV>> addNews(
            @PathVariable("newsId") UUID newsId,
            @CurrentUser Jwt jwt) {
        HistoryMV historyMV = historyService.addHistory(newsId, jwt.getSubject());
        ApiVM<HistoryMV> apiVM = new ApiVM<>(historyMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @GetMapping("")
    public ResponseEntity<ApiVM<PaginationMV<HistoryWithNewsMV>>> saveFavouriteNewspaper(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_ID) String sortBy,
            @CurrentUser Jwt jwt) {
        PaginationVM paginationVM = new PaginationVM(page, limit, sortDir, sortBy);
        PaginationMV<HistoryWithNewsMV> paginationMV = historyService.getHistories(paginationVM, jwt.getSubject());
        ApiVM<PaginationMV<HistoryWithNewsMV>> apiVM = new ApiVM<>(paginationMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @DeleteMapping("{historyId}")
    public ResponseEntity<ApiVM<UUID>> deleteNews(
            @PathVariable("historyId") UUID historyId,
            @CurrentUser Jwt jwt) {
        historyService.deleteHistory(historyId, jwt.getSubject());
        ApiVM<UUID> apiVM = new ApiVM<>(historyId);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}

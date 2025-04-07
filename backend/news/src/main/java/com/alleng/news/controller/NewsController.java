package com.alleng.news.controller;

import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.news.constant.PaginationConstant;
import com.alleng.news.payload.request.NewsVM;
import com.alleng.news.payload.request.PaginationVM;
import com.alleng.news.payload.response.NewsMV;
import com.alleng.news.payload.response.PaginationMV;
import com.alleng.news.service.INewsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1/news}/news")
public class NewsController {

    INewsService newsService;

    @GetMapping("/list")
    public ResponseEntity<ApiVM<PaginationMV<NewsMV>>> getNewsList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_ID) String sortBy,
            @RequestParam(name = "topic", defaultValue = PaginationConstant.DEFAULT_ALL) String topic,
            @RequestParam(name = "source", defaultValue = PaginationConstant.DEFAULT_ALL) String source,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "date", required = false) String date
    ) {
        PaginationMV<NewsMV> paginationMV;
        if (date != null) {
            PaginationVM paginationVM = new PaginationVM(page, limit, sortDir, sortBy, topic, source, null, date);
            paginationMV = newsService.findByNewspaperListByDate(paginationVM);
        } else {
            PaginationVM paginationVM = new PaginationVM(page, limit, sortDir, sortBy, topic, source, search, null);
            paginationMV = newsService.findByNewspaperList(paginationVM);
        }
        ApiVM<PaginationMV<NewsMV>> apiVM = new ApiVM<>(paginationMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping(value = "/list/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiVM<List<NewsMV>>> getListFavorite(@RequestParam List<UUID> ids) {
        List<NewsMV> mvList = newsService.getListFavorite(ids);
        ApiVM<List<NewsMV>> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping(value = "/list/rand", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiVM<List<NewsMV>>> getListRand(@RequestParam(name = "rand", defaultValue = PaginationConstant.DEFAULT_LIMIT) int rand) {
        List<NewsMV> mvList = newsService.getListRand(rand);
        ApiVM<List<NewsMV>> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @GetMapping(value = "/{newsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiVM<NewsMV>> getNews(@PathVariable(name = "newsId") UUID newsId) {
        NewsMV mvList = newsService.getNews(newsId);
        ApiVM<NewsMV> apiVM = new ApiVM<>(mvList);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PreAuthorize("WRITE_NEWS")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiVM<NewsMV>> createNews(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "audio") MultipartFile audio,
            @RequestPart(name = "news") NewsVM newsVM) {
        try {

            NewsMV newsMV = newsService.create(image, audio, newsVM, authorizationHeader);
            ApiVM<NewsMV> apiVM = new ApiVM<>(newsMV);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiVM<>(e.getMessage(), null));
        }
    }

    @PreAuthorize("UPDATE_NEWS")
    @PutMapping("/{newsId}")
    public ResponseEntity<ApiVM<NewsMV>> updateNews(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable(name = "newsId") UUID newsId,
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart(name = "audio", required = false) MultipartFile audio,
            @RequestPart(name = "news") NewsVM newsVM) {
        try {
            NewsMV newsMV = newsService.update(newsId, newsVM, image, audio, authorizationHeader);
            ApiVM<NewsMV> apiVM = new ApiVM<>(newsMV);
            return ResponseEntity.status(HttpStatus.OK).body(apiVM);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiVM<>(e.getMessage(), null));
        }
    }

    @PreAuthorize("DELETE_NEWS")
    @DeleteMapping("")
    public ResponseEntity<ApiVM<List<UUID>>> updateNews(@RequestBody List<UUID> newsIdList) {
        List<UUID> list = newsService.delete(newsIdList);
        ApiVM<List<UUID>> apiVM = new ApiVM<>(list);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }
}

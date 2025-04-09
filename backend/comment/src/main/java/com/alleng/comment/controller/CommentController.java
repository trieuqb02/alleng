package com.alleng.comment.controller;

import com.alleng.comment.constant.CurrentUser;
import com.alleng.comment.payload.request.CommentVM;
import com.alleng.comment.payload.response.CommentMV;
import com.alleng.comment.service.ICommentService;
import com.alleng.commonlibrary.constant.PaginationConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.commonlibrary.payload.PaginationMV;
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
@RequestMapping("${com.alleng.prefix.api:/api/v1}/comment/")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentController {

    ICommentService commentService;

    @GetMapping("news/{newsId}")
    public ResponseEntity<ApiVM<PaginationMV<CommentMV>>> getCommentList(
            @PathVariable("newsId") UUID newsId,
            @RequestParam(value = "parentId", required = false) UUID parentId,
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit
    ) {
        PaginationMV<CommentMV> paginationMV = commentService.getCommentListOfNews(page, limit ,newsId, parentId);
        ApiVM<PaginationMV<CommentMV>> apiVM = new ApiVM<>(paginationMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PostMapping("news/{newsId}")
    public ResponseEntity<ApiVM<CommentMV>> createComment(
            @PathVariable("newsId") UUID newsId,
            @CurrentUser Jwt jwt,
            @RequestBody CommentVM vm
    ) {
        CommentMV commentMV = commentService.createComment(newsId, jwt.getSubject(), vm);
        ApiVM<CommentMV> apiVM = new ApiVM<>(commentMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<ApiVM<?>> updateComment(
            @PathVariable("commentId") UUID commentId,
            @CurrentUser Jwt jwt,
            @RequestBody CommentVM vm) {
        CommentMV commentMV = commentService.updateComment(commentId, jwt.getSubject(), vm);
        ApiVM<CommentMV> apiVM = new ApiVM<>(commentMV);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiVM);
    }
}

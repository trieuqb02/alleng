package com.alleng.comment.service;

import com.alleng.comment.payload.request.CommentVM;
import com.alleng.comment.payload.response.CommentMV;
import com.alleng.commonlibrary.payload.PaginationMV;

import java.util.UUID;

public interface ICommentService {
    CommentMV createComment(UUID newsId, String subject, CommentVM vm);

    CommentMV updateComment(UUID commentId, String subject, CommentVM vm);

    PaginationMV<CommentMV> getCommentListOfNews(int page, int limit, UUID newsId, UUID parentId);
}

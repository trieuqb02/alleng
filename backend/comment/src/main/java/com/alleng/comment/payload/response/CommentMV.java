package com.alleng.comment.payload.response;

import com.alleng.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentMV(UUID uuid, UUID parentId, String content, LocalDateTime commentAt, UserMV user,
                        long totalSubComment) {
    public static CommentMV convertCommentMV(Comment comment, UserMV user, long totalSubComment) {
        return new CommentMV(comment.getId(), comment.getParentId(), comment.getContent(), comment.getCommentAt(), user, totalSubComment);
    }
}

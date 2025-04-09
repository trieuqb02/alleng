package com.alleng.comment.payload.request;

import com.alleng.comment.constant.StatusEnum;

import java.util.UUID;

public record CommentVM(UUID parentId, String content, StatusEnum status) {
}

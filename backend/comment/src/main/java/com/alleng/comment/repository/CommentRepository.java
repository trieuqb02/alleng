package com.alleng.comment.repository;

import com.alleng.comment.constant.StatusEnum;
import com.alleng.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    long countByParentId(UUID uuid);

    Page<Comment> findAllByNewsIdAndStatusAndParentId(UUID newsId, StatusEnum status, UUID parentId, Pageable pageable);
}
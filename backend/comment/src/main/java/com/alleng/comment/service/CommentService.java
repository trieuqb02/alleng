package com.alleng.comment.service;

import com.alleng.comment.constant.StatusEnum;
import com.alleng.comment.entity.Comment;
import com.alleng.comment.feign.IdentityClient;
import com.alleng.comment.payload.request.CommentVM;
import com.alleng.comment.payload.response.CommentMV;
import com.alleng.comment.payload.response.UserMV;
import com.alleng.comment.repository.CommentRepository;
import com.alleng.commonlibrary.constant.PaginationConstant;
import com.alleng.commonlibrary.payload.PaginationMV;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    CommentRepository commentRepository;

    IdentityClient identityClient;

    @Override
    public CommentMV createComment(UUID newsId, String subject, CommentVM vm) {
        Comment comment = Comment.builder()
                .newsId(newsId)
                .username(subject)
                .content(vm.content())
                .parentId(vm.parentId())
                .commentAt(LocalDateTime.now())
                .status(StatusEnum.ACTIVE)
                .build();

        comment = commentRepository.save(comment);

        List<UserMV> list = Objects.requireNonNull(identityClient.getListUser(List.of(comment.getUsername())).getBody()).data();

        return CommentMV.convertCommentMV(comment, list.getFirst(), 0);
    }

    @Override
    public CommentMV updateComment(UUID commentId, String subject, CommentVM vm) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setStatus(vm.status());
        commentRepository.save(comment);

        comment = commentRepository.save(comment);

        long totalSubComment = commentRepository.countByParentId(commentId);

        List<UserMV> list = Objects.requireNonNull(identityClient.getListUser(List.of(comment.getUsername())).getBody()).data();

        return CommentMV.convertCommentMV(comment, list.getFirst(), totalSubComment);
    }

    @Override
    public PaginationMV<CommentMV> getCommentListOfNews(int page, int limit, UUID newsId, UUID parentId) {
        Sort sort = Sort.by(PaginationConstant.DEFAULT_ID).ascending();

        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        System.out.println();
        Page<Comment> resultPage = commentRepository.findAllByNewsIdAndStatusAndParentId(newsId, StatusEnum.ACTIVE, parentId, pageable);

        List<Comment> comments = resultPage.getContent();

        List<String> ids = comments.stream().map(Comment::getUsername).toList();

        List<UserMV> userMVList = Objects.requireNonNull(identityClient.getListUser(ids).getBody()).data();

        Map<String, List<UserMV>> userMVMap = userMVList.stream()
                .collect(Collectors.groupingBy(UserMV::fullName));

        List<CommentMV> collect = comments.stream()
                .map(comment -> {
                    List<UserMV> userList = userMVMap.get(comment.getUsername());
                    UserMV userMV = (userList != null && !userList.isEmpty()) ? userList.getFirst() : null;

                    long totalSubComment = commentRepository.countByParentId(comment.getId());

                    return new CommentMV(
                            comment.getId(),
                            comment.getParentId(),
                            comment.getContent(),
                            comment.getCommentAt(),
                            userMV,
                            totalSubComment
                    );
                })
                .toList();

        return new PaginationMV<>(collect, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(), resultPage.getTotalElements(), resultPage.isLast());
    }

}

package com.alleng.comment.entity;

import com.alleng.comment.constant.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Comment extends Auditor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID newsId;

    private String username;

    private UUID parentId;

    private String content;

    private LocalDateTime commentAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}

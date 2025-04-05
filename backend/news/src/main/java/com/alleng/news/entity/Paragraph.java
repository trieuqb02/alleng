package com.alleng.news.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Paragraph extends AbstractVersion{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String content;

    @ManyToOne(optional = false)
    private News news;

    public Paragraph(String content, News news) {
        this.content = content;
        this.news = news;
    }

    public Paragraph(Paragraph paragraph, News news) {
        this.setId(paragraph.getId());
        this.setContent(paragraph.getContent());
        this.setVersion(paragraph.getVersion());
        this.setNews(news);
    }
}

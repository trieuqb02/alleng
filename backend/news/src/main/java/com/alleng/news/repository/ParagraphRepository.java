package com.alleng.news.repository;

import com.alleng.news.entity.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParagraphRepository extends JpaRepository<Paragraph, UUID> {
}
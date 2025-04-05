package com.alleng.news.repository;

import com.alleng.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {

    Page<News> findByPublicationDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query("SELECT e FROM News e ORDER BY function('RAND')")
    List<News> findRandomNews(Pageable pageable);
}
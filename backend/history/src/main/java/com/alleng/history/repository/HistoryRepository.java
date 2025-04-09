package com.alleng.history.repository;

import com.alleng.history.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID> {
    Long countByUsernameAndReadAtBetween(String username, LocalDateTime start, LocalDateTime end);
    Page<History> findAllByUsername(String subject, Pageable pageable);
}
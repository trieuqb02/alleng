package com.alleng.history.repository;

import com.alleng.history.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID> {
    Page<History> findAllByUsername(String subject, Pageable pageable);
}
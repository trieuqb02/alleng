package com.alleng.file.repository;

import com.alleng.file.entity.Idempotent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotentRepository extends JpaRepository<Idempotent, String> {
}
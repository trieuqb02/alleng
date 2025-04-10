package com.alleng.favorite.repository;

import com.alleng.favorite.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Page<Favorite> findAllByUserId(UUID userId, Pageable pageable);
}
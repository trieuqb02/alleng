package com.alleng.news.repository;

import com.alleng.news.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
    boolean existsByName(String name);

    Optional<Topic> findByName(String name);

    @Query("SELECT t.id ,t.name, t.description, t.enable, COUNT(n) FROM Topic t LEFT JOIN t.news n GROUP BY t.id, t.name")
    List<Object[]> findTopicsWithNewsCount();
}
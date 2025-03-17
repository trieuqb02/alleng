package com.alleng.identity.repository;

import com.alleng.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
     boolean existsByUsername(String username);

     Optional<User> findByUsername(String username);
}
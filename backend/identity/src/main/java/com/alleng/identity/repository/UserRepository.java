package com.alleng.identity.repository;

import com.alleng.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
     boolean existsByUsername(String username);

     Optional<User> findByUsername(String username);

     @Query("SELECT u.id, u.fullName, u.thumbnail FROM User u where u.id = ?1")
     Object findById2(UUID userId);

}
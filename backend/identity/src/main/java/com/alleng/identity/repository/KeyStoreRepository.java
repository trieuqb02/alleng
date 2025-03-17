package com.alleng.identity.repository;

import com.alleng.identity.entity.KeyStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KeyStoreRepository extends JpaRepository<KeyStore, UUID> {
}
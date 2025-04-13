package com.alleng.subscription.repository;

import com.alleng.subscription.constant.CodeType;
import com.alleng.subscription.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    Boolean existsByCode(CodeType code);
}
package com.alleng.subscription.entity;

import com.alleng.subscription.constant.CodeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private CodeType code;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "features")
    private Set<Plan> plans = new HashSet<>();
}

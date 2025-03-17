package com.alleng.identity.entity;

import com.alleng.identity.constant.Provider;
import com.alleng.identity.constant.StatusType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class User extends AbstractVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String email;

    private String fullName;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.DEFAULT;

    @Enumerated(EnumType.STRING)
    private StatusType statusType = StatusType.ONLINE;

    @OneToOne(mappedBy = "user")
    private KeyStore keyStore;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}

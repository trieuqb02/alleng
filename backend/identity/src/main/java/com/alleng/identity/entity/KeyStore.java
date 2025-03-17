package com.alleng.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KeyStore {

    @Id
    private UUID id;;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private Instant expiredIn;

    @Column(columnDefinition = "TEXT")
    private String publicKey;

    @Column(columnDefinition = "TEXT")
    private String privateKey;

    @OneToOne(optional = false)
    @MapsId
    private User user;

}

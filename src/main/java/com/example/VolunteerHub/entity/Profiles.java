package com.example.VolunteerHub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profiles {
    @Id
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "avatar_url")
    @Builder.Default
    String avatarUrl = null;

    @Column(name = "bio")
    @Builder.Default
    String bio = null;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "created_at")
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    // users
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    Users user;
}

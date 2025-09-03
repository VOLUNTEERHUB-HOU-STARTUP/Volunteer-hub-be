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

    @Column(name = "last_updated_name")
    @Builder.Default
    LocalDateTime lastUpdatedName = LocalDateTime.now();

    @Column(name = "avatar_url")
    @Builder.Default
    String avatarUrl = null;

    @Column(name = "bio")
    @Builder.Default
    String bio = null;

    @Builder.Default
    @Column(name = "is_active")
    boolean isActive = true;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "created_at")
    LocalDateTime createdAt; // YYYY-MM-ddTHH:mm:ss

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // users
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    Users user;
}

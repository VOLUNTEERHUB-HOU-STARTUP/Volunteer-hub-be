package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.AuthProviderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    AuthProviderEnum provider; // LOCAL, FB, GOOGLE

    @Column(name = "provider_id")
    String providerId;  // email sub v fb id

    @Column(name = "password")
    String password; // set khi local

    @Column(name = "avatar_url")
    String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;
}

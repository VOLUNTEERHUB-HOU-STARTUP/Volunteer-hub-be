package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.entity.key.UserRolesKey;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "created_at")
    Instant createdAt;

    // role
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserRoles> userRoles = new ArrayList<>();
}

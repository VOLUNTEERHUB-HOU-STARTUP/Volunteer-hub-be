package com.example.VolunteerHub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "password")
    String password;

    // profiles
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Profiles profile;

    // roles
    @ManyToOne
    @JoinColumn(name = "role_id")
    Roles role;

    // events
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Events> events = new ArrayList<>();

    // event volunteer
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventVolunteers> eventVolunteers;
}

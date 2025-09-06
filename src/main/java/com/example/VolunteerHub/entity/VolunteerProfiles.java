package com.example.VolunteerHub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VolunteerProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    Users user;

    @Column(name = "total_event_joined")
    int totalEventJoined;

    @Column(name = "total_event_registered")
    int totalEventRegistered;

    @Column(name = "rating")
    float rating;
}

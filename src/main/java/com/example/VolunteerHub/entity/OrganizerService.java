package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.ServiceEnum;
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
public class OrganizerService {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    Users organizer;

    @ManyToOne
    @JoinColumn(name = "service_id")
    Services service;

    int remainingPost;
    LocalDateTime startDate;
    LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    ServiceEnum status;
}

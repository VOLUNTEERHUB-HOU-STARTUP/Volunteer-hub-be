package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.VolunteerStatusEnum;
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
public class EventVolunteers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Events event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    VolunteerStatusEnum status;

    @Builder.Default
    @Column(name = "qr_code", unique = true)
    String qrCode = null;

    @Column(name = "is_checked_in")
    boolean isCheckedIn;

    @Column(name = "check_in_time")
    LocalDateTime checkInTime;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}

package com.example.VolunteerHub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"volunteer_id", "organizer_id"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VolunteerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    Users volunteer;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    Users organizer;

    @Column(nullable = false)
    int rating;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}

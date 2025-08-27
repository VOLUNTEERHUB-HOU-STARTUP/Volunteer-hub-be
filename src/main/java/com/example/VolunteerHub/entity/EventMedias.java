package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.MediaTypeEnum;
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
public class EventMedias {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Events event;

    @Column(name = "media_type")
    MediaTypeEnum mediaType;

    @Column(name = "media_url")
    String mediaUrl;
}

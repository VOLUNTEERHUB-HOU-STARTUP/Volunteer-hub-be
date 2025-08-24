package com.example.VolunteerHub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    UUID id;
    UUID userId;
    String fullName;
    String avatarUrl;
    String title;
    String slug;
    String description;
    LocalDateTime startAt;
    LocalDateTime endAt;
    int maxVolunteer;
    boolean isPublished;
}

package com.example.VolunteerHub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
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
    float salary;
    String location;
    List<EventMediaResponse> eventMedia;
    LocalDateTime startAt;
    LocalDateTime endAt;
    int maxVolunteer;
    boolean isPublished;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;
}

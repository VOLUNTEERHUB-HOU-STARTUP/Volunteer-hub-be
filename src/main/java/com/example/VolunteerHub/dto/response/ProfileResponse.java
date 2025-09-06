package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.entity.VolunteerProfiles;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    UUID userId;
    String fullName;
    String avatarUrl;
    String bio;
    boolean isActive;
    VolunteerProfileResponse volunteerProfileResponse;
    OrganizerProfileResponse organizerProfileResponse;
    LocalDateTime updatedAt;
    LocalDateTime createdAt; // YYYY-MM-ddTHH:mm:ss
}

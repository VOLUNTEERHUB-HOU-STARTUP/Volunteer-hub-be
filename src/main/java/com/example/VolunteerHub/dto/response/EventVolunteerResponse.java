package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.enums.VolunteerStatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventVolunteerResponse {
    UUID id;
    UUID eventId;
    UUID userId;
    String fullName;
    String avatarUrl;
    boolean isCheckedIn;
    VolunteerStatusEnum status;
    LocalDateTime checkInTime;
    LocalDateTime createdAt;
}

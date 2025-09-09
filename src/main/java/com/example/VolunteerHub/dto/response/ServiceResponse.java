package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.enums.ServiceEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    UUID id;
    String title;
    int maxPosts;
    int durationInDays;
}

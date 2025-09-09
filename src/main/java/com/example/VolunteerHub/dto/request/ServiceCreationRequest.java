package com.example.VolunteerHub.dto.request;

import com.example.VolunteerHub.enums.ServiceEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceCreationRequest {
    String title;
    int maxPosts;
    int durationInDays;
}

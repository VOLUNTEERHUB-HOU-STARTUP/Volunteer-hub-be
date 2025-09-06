package com.example.VolunteerHub.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VolunteerProfileResponse {
    int totalEventJoined;
    int totalEventRegistered;
    float rating;
}

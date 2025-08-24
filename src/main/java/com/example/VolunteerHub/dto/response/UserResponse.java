package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    UUID id;
    String email;
    String fullName;
    boolean isActive;
    LocalDateTime createdAt;
    RoleEnum role;
}

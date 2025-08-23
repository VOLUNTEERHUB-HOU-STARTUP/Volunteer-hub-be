package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.enums.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String email;
    String fullName;
    boolean isActive;
    Instant createdAt;
    RoleEnum role;
}

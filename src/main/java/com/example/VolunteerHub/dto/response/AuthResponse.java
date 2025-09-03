package com.example.VolunteerHub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String jwt;
    Map<String, Objects> user;
}

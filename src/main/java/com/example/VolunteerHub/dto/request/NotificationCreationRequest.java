package com.example.VolunteerHub.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationCreationRequest {
    String title;
    LocalDateTime time;
    boolean read;
    String link;
}

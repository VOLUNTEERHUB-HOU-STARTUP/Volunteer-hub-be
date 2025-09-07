package com.example.VolunteerHub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PendingQueueResponse {
    int events;
    int connections;
    int content;
    int reports;
    int support;
    int admin;
    int user;
    int partners;
    int services;
    int systemAlerts;
    int backup;
    int setting;
}

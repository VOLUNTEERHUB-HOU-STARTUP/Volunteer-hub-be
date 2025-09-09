package com.example.VolunteerHub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PendingQueueResponse {
    int events; // event
    int connections; // message
    int content;
    int reports; // report
    int support;
    int admin;
    int user; // volunteer
    int partners; // organizer
    int services;

    int systemAlerts;
    int backup;
    int setting;
}

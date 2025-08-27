package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.enums.MediaTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventMediaResponse {
    UUID id;
    String mediaUrl;
    MediaTypeEnum mediaType;
}

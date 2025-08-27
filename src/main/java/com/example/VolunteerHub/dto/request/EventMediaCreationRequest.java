package com.example.VolunteerHub.dto.request;

import com.example.VolunteerHub.enums.MediaTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventMediaCreationRequest {
    MediaTypeEnum mediaType;
    String mediaUrl;
}

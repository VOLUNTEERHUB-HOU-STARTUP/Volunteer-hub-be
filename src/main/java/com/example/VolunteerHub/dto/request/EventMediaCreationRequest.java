package com.example.VolunteerHub.dto.request;

import com.example.VolunteerHub.enums.MediaTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventMediaCreationRequest {
    MultipartFile media;
}

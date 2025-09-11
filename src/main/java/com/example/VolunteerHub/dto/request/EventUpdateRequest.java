package com.example.VolunteerHub.dto.request;

import com.example.VolunteerHub.enums.EventTypeEnum;
import com.example.VolunteerHub.enums.ExperienceEnum;
import com.example.VolunteerHub.enums.PriorityEnum;
import com.example.VolunteerHub.enums.SexEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateRequest {
    String title;
    String description;
    float salary;
    String location;
    String detailLocation;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime deadline;
    int maxVolunteer;

    int minAge;
    int maxAge;
    SexEnum sex;
    ExperienceEnum experience;
    String coverImage;
    boolean online;

    String leaderName;
    String leaderPhone;
    String leaderEmail;
    String subContact;

    PriorityEnum priority;
    EventTypeEnum type;

    List<String> categories;
    List<String> skills;
    List<String> tags;

    List<UUID> listDeleteMediaId;
    List<MultipartFile> listEventMedia;
}

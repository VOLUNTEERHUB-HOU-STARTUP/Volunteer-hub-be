package com.example.VolunteerHub.dto.response;

import com.example.VolunteerHub.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    UUID id;
    UUID userId;
    String fullName;
    String avatarUrl;
    String title;
    String slug;
    String description;
    float salary;
    String location;
    String detailLocation;

    List<EventMediaResponse> eventMedia;
    LocalDateTime startAt;
    LocalDateTime endAt;
    int maxVolunteer;
    boolean autoAccept;
    EventStatusEnum status;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;
    LocalDateTime deadline;

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

    PriorityEnum priority; // mức khẩn cấp
    EventTypeEnum type; // loại (lợi nhuận/ phi lợi nhuận)

    List<CategoryResponse> categories; // phân loại skien
    List<RequiredSkillResponse> skills; // skill cần thiết
    List<TypeTagResponse> tags; // thẻ tag phân loại sk
    List<InterestResponse> interests; // lợi iích khi tham gia

    int totalLike;
    int totalComment;
}

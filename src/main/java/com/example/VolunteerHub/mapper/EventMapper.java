package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.response.EventMediaResponse;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.EventStatusEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper
public class EventMapper {
    public static Events mapToEntity(EventCreationRequest request, Users user, boolean isDraft) {
        return Events.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .salary(request.getSalary())
                .location(request.getLocation())
                .detailLocation(request.getDetailLocation())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .deadline(request.getDeadline())
                .autoAccept(request.isAutoAccept())
                .type(request.getType())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .sex(request.getSex())
                .experience(request.getExperience())
                .coverImage(request.getCoverImage())
                .online(request.isOnline())
                .leaderName(request.getLeaderName())
                .leaderPhone(request.getLeaderPhone())
                .leader_email(request.getLeaderEmail())
                .subContact(request.getSubContact())
                .priority(request.getPriority())
                .status(isDraft
                        ? EventStatusEnum.DRAFT
                        : user.getRole().getRole() == RoleEnum.ADMIN
                            ? EventStatusEnum.IS_PUBLISHED
                            : EventStatusEnum.PENDING
                )
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static EventResponse mapToResponse(Events event) {
        return EventResponse.builder()
                .id(event.getId())
                .userId(event.getUser().getId())
                .fullName(event.getUser().getProfile().getFullName())
                .avatarUrl(event.getUser().getProfile().getAvatarUrl())
                .title(event.getTitle())
                .description(event.getDescription())
                .slug(event.getSlug())
                .salary(event.getSalary())
                .location(event.getLocation())
                .status(event.getStatus())
                .maxVolunteer(event.getMaxVolunteer())
                .eventMedia(event.getEventMedia().stream().map(media ->
                        EventMediaResponse.builder()
                                .id(media.getId())
                                .mediaType(media.getMediaType())
                                .mediaUrl(media.getMediaUrl())
                                .build()).toList())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .deadline(event.getDeadline())
                .autoAccept(event.isAutoAccept())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}

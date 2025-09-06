package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.response.EventMediaResponse;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.entity.Users;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper
public class EventMapper {
    public static Events mapToEntity(EventCreationRequest request, Users user) {
        return Events.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .salary(request.getSalary())
                .location(request.getLocation())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .isPublished(false)
                .deadline(request.getDeadline())
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
                .isPublished(event.isPublished())
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
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}

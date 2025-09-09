package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.response.EventVolunteerResponse;
import com.example.VolunteerHub.entity.EventVolunteers;
import org.mapstruct.Mapper;

@Mapper
public class EventVolunteerMapper {
    public static EventVolunteerResponse toEventVolunteerResponse(EventVolunteers ev) {
        return EventVolunteerResponse.builder()
                .id(ev.getId())
                .userId(ev.getUser().getId())
                .avatarUrl(ev.getUser().getProfile().getAvatarUrl())
                .fullName(ev.getUser().getProfile().getFullName())
                .eventId(ev.getEvent().getId())
                .isCheckedIn(ev.isCheckedIn())
                .status(ev.getStatus())
                .checkInTime(ev.getCheckInTime())
                .createdAt(ev.getCreatedAt())
                .build();
    }
}

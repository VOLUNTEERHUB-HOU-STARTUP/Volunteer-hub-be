package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.response.OrganizerProfileResponse;
import com.example.VolunteerHub.dto.response.ProfileResponse;
import com.example.VolunteerHub.dto.response.VolunteerProfileResponse;
import com.example.VolunteerHub.entity.OrganizerProfiles;
import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.VolunteerProfiles;
import org.mapstruct.Mapper;

@Mapper
public class ProfileMapper {
    public static ProfileResponse toProfileResponse(Profiles profile, VolunteerProfiles volunteerProfile, OrganizerProfiles organizerProfile) {
        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .avatarUrl(profile.getAvatarUrl())
                .fullName(profile.getFullName())
                .isActive(profile.isActive())
                .bio(profile.getBio())
                .volunteerProfileResponse(toVolunteerProfileResponse(volunteerProfile))
                .organizerProfileResponse(toOrganizerProfileResponse(organizerProfile))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    public static VolunteerProfileResponse toVolunteerProfileResponse(VolunteerProfiles volunteerProfileResponse) {
        if (volunteerProfileResponse == null) return null;

        return VolunteerProfileResponse.builder()
                .totalEventRegistered(volunteerProfileResponse.getTotalEventRegistered())
                .totalEventJoined(volunteerProfileResponse.getTotalEventJoined())
                .rating(volunteerProfileResponse.getRating())
                .build();
    }

    public static OrganizerProfileResponse toOrganizerProfileResponse(OrganizerProfiles organizerProfileResponse) {
        if (organizerProfileResponse == null) return null;

        return OrganizerProfileResponse.builder()
                .totalEventOrganized(organizerProfileResponse.getTotalEventOrganized())
                .totalParticipants(organizerProfileResponse.getTotalParticipants())
                .build();
    }
}

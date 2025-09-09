package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.response.ProfileResponse;
import com.example.VolunteerHub.entity.EventVolunteers;
import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.ProfileMapper;
import com.example.VolunteerHub.repository.EventVolunteerRepository;
import com.example.VolunteerHub.repository.ProfileRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecommendationService {
    UserRepository userRepository;
    EventVolunteerRepository eventVolunteerRepository;
    ProfileRepository profileRepository;

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<ProfileResponse> getRecommendations(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(0, size);
        Page<EventVolunteers> latestVolunteers = eventVolunteerRepository
                .findLatestVolunteersByOrganizer(organizer.getId(), pageable);

        return latestVolunteers.stream()
                .map(ev -> {
                    Profiles profile = profileRepository.findByUserId(ev.getUser().getId());
                    return ProfileMapper.toProfileResponse(profile, null, null, 0);
                }).toList();
    }
}

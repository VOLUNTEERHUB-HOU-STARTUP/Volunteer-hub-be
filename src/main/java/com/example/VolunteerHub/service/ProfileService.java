package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ProfileUpdateRequest;
import com.example.VolunteerHub.dto.response.ProfileResponse;
import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.ProfileMapper;
import com.example.VolunteerHub.repository.ProfileRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    UserRepository userRepository;

    public void updateProfile(ProfileUpdateRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Profiles profile = profileRepository.findByUserId(user.getId());

        if (request.getAvatarUrl() != null)
            profile.setAvatarUrl(request.getAvatarUrl());

        if (request.getBio() != null)
            profile.setBio(request.getBio());

        if (request.getFullName() != null) {
            long timeLastChange =
                    Duration.between(LocalDateTime.now(), profile.getLastUpdatedName()).getSeconds();
            long timeLimit = 30L * 24 * 60 * 60; // thời gian đc đổi tên cách 1 tháng

            if (profile.getLastUpdatedName() != null && timeLastChange  < timeLimit)
                throw new AppException(ErrorCode.NAME_CHANGE_TOO_SOON);

            profile.setFullName(request.getFullName());
            profile.setLastUpdatedName(LocalDateTime.now());
        }

        profile.setUpdatedAt(LocalDateTime.now());
        profileRepository.save(profile);
    }

    public ProfileResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Profiles profile = profileRepository.findByUserId(user.getId());

        return ProfileMapper
                .toProfileResponse(profile, user.getVolunteerProfile(), user.getOrganizerProfile());
    }
}

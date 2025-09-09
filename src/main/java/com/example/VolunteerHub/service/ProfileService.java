package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ProfileUpdateRequest;
import com.example.VolunteerHub.dto.response.*;
import com.example.VolunteerHub.entity.OrganizerProfiles;
import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.entity.VolunteerProfiles;
import com.example.VolunteerHub.enums.AccountStatusEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.ProfileMapper;
import com.example.VolunteerHub.repository.*;
import com.example.VolunteerHub.utils.AuthUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    VolunteerProfileRepository volunteerProfileRepository;
    OrganizerProfileRepository organizerProfileRepository;
    VolunteerRatingRepository volunteerRatingRepository;
    UserService userService;
    UserRepository userRepository;
    RoleRepository roleRepository;
    EventService eventService;
    EventVolunteerOfEventService eventVolunteerOfEventService;

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
            if (profile.getLastUpdatedName() != null) {
                long timeLastChange = Duration.between(profile.getLastUpdatedName(), LocalDateTime.now()).getSeconds();
                long timeLimit = 30L * 24 * 60 * 60;// thời gian đc đổi tên cách 1 tháng

                if (timeLastChange < timeLimit) {
                    throw new AppException(ErrorCode.NAME_CHANGE_TOO_SOON);
                }
            }

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
        VolunteerProfiles volunteerProfile = volunteerProfileRepository.findByUserId(user.getId());
        OrganizerProfiles organizerProfile = organizerProfileRepository.findByUserId(user.getId());

        float rating = 0;

        if (volunteerProfile != null)
            rating = volunteerRatingRepository.getAverageRating(user.getId());

        return ProfileMapper.toProfileResponse(profile, volunteerProfile, organizerProfile, formatRating(rating));
    }

    public ProfileResponse getProfileWithUserId(UUID userId) {
        Users user = userService.getUserById(userId);

        if (user.getRole().getRole() == RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Profiles profile = profileRepository.findByUserId(userId);
        VolunteerProfiles volunteerProfile = volunteerProfileRepository.findByUserId(userId);
        OrganizerProfiles organizerProfile = organizerProfileRepository.findByUserId(userId);

        float rating = 0;

        if (volunteerProfile != null)
            rating = volunteerRatingRepository.getAverageRating(user.getId());

        return ProfileMapper.toProfileResponse(profile, volunteerProfile, organizerProfile, formatRating(rating));
    }

    public List<ProfileResponse> getListVolunteer(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Users> usersList =
                userRepository.findByRole(roleRepository.findByRole(RoleEnum.VOLUNTEER).getId(), pageable);

        return usersList.map(user -> getProfileWithUserId(user.getId())).toList();
    }

    public List<ProfileResponse> getListOrganizer(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Users> usersList =
                userRepository.findByRole(roleRepository.findByRole(RoleEnum.ORGANIZER).getId(), pageable);

        return usersList.map(user -> getProfileWithUserId(user.getId())).toList();
    }

    public List<ProfileResponse> getListVolunteerOfEvent(String slug, int page, int size) {
        EventResponse eventResponse = eventService.getEventBySlug(slug);

        List<EventVolunteerResponse> listVolunteer =
                eventVolunteerOfEventService.getListEventVolunteer(eventResponse.getId(), page, size);

        List<UUID> userIdList = new ArrayList<>();

        for (EventVolunteerResponse eventVolunteerResponse : listVolunteer)
            userIdList.add(eventVolunteerResponse.getUserId());

        return userIdList.stream().map(this::getProfileWithUserId).toList();
    } //

    private float formatRating(Float rating) {
        if (rating == null) return 0f;
        return (float) (Math.round(rating * 100.0) / 100.0); // giữ 2 chữ số sau dấu phẩy
    }
}

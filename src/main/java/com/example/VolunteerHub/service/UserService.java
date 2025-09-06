package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.UserChangeRoleRequest;
import com.example.VolunteerHub.dto.request.UserCreationRequest;
import com.example.VolunteerHub.dto.request.UserDeleteAccountRequest;
import com.example.VolunteerHub.dto.response.UserCreationResponse;
import com.example.VolunteerHub.dto.response.UserResponse;
import com.example.VolunteerHub.entity.*;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.UserMapper;
import com.example.VolunteerHub.repository.ProfileRepository;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserRepository;
import com.example.VolunteerHub.utils.AuthUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.VolunteerHub.enums.RoleEnum.VOLUNTEER;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileRepository profileRepository;
    AuthUtil authUtil;

    @Transactional
    public UserCreationResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        if (request.getRole() == RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Roles role = roleRepository.findByRole(request.getRole());

        if (role == null)
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);

        Users user = Users.builder()
                .email(request.getEmail())
                .role(role)
                .build();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Profiles profile = Profiles.builder()
                .user(user)
                .fullName(request.getFullName())
                .build();

        switch (user.getRole().getRole()) {
            case VOLUNTEER -> {
                VolunteerProfiles volunteerProfile = VolunteerProfiles.builder()
                        .user(user)
                        .rating(0)
                        .totalEventJoined(0)
                        .totalEventRegistered(0)
                        .build();
                user.setVolunteerProfile(volunteerProfile);
            }
            case ORGANIZER -> {
                OrganizerProfiles organizerProfile = OrganizerProfiles.builder()
                        .user(user)
                        .totalEventOrganized(0)
                        .totalParticipants(0)
                        .build();
                user.setOrganizerProfile(organizerProfile);
            }
        }

        user.setProfile(profile);

        userRepository.save(user);

        return UserCreationResponse.builder()
                .success(true)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getListUser() {
        List<Users> usersList = userRepository.findAll();

        return usersList.stream().map(UserMapper::toUserResponse).toList();
    }

    public UserResponse getMyInfo() {
        Users user = authUtil.getCurrentUser();

        return UserMapper.toUserResponse(user);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void changeUserRole(UserChangeRoleRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Roles role = roleRepository.findByRole(request.getNewRole());

        if (role.getRole() == RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        switch (role.getRole()) {
            case ORGANIZER -> {
                user.setVolunteerProfile(null);
                user.setOrganizerProfile(OrganizerProfiles.builder()
                                .user(user)
                                .totalParticipants(0)
                                .totalEventOrganized(0)
                        .build());
            }
            case VOLUNTEER -> {
                user.setOrganizerProfile(null);
                user.setVolunteerProfile(VolunteerProfiles.builder()
                                .user(user)
                                .totalEventJoined(0)
                                .totalEventRegistered(0)
                                .rating(0)
                        .build());
            }
        }

        user.setRole(role);
        userRepository.save(user);
    }

    public void deleteAccount(UserDeleteAccountRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (currentUser.getRole().getRole() == RoleEnum.ADMIN) {
            Users targetUser = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            if (targetUser.getRole().getRole() == RoleEnum.ADMIN) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            userRepository.delete(targetUser);
            return;
        }

        if (!email.equals(request.getEmail())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        userRepository.delete(currentUser);
    }
}

package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.UserChangeRoleRequest;
import com.example.VolunteerHub.dto.request.UserCreationRequest;
import com.example.VolunteerHub.dto.request.UserDeleteAccountRequest;
import com.example.VolunteerHub.dto.response.UserCreationResponse;
import com.example.VolunteerHub.dto.response.UserResponse;
import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.Roles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.ProfileRepository;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileRepository profileRepository;

    @Transactional
    public UserCreationResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_NOT_EXISTED);

        if (request.getRole() == RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Roles role = roleRepository.findByRole(request.getRole());

        Users user = Users.builder()
                .email(request.getEmail())
                .role(role)
                .build();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        Profiles profile = Profiles.builder()
                .user(user)
                .fullName(request.getFullName())
                .build();

        profileRepository.save(profile);

        return UserCreationResponse.builder()
                .success(true)
                .build();
    }

    public List<UserResponse> getListUser() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var role = user.getRole().getRole();
        if (role != RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        List<Users> usersList = userRepository.findAll();

        return usersList.stream().map(u -> UserResponse.builder()
                .id(u.getId())
                .email(u.getEmail())
                .fullName(u.getProfile().getFullName())
                .isActive(u.getProfile().isActive())
                .createdAt(u.getProfile().getCreatedAt())
                .build()).toList();
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Roles role = roleRepository.findByRole(user.getRole().getRole());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getProfile().getFullName())
                .role(role.getRole())
                .isActive(user.getProfile().isActive())
                .createdAt(user.getProfile().getCreatedAt())
                .build();
    }

    public void changeUserRole(UserChangeRoleRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var adminRole = admin.getRole().getRole();
        if (adminRole != RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Roles role = roleRepository.findByRole(request.getNewRole());

        if (role.getRole() == RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

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

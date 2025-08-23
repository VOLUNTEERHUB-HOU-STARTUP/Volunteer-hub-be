package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.UserCreationRequest;
import com.example.VolunteerHub.dto.response.UserCreationResponse;
import com.example.VolunteerHub.dto.response.UserResponse;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserCreationResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("user existed");

        var role = roleRepository.findByRole(RoleEnum.VOLUNTEER);

        Users user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(role)
                .isActive(true)
                .createdAt(Instant.now())
                .build();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

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
                .fullName(u.getFullName())
                .isActive(u.isActive())
                .createdAt(u.getCreatedAt())
                .build()).toList();
    }
}

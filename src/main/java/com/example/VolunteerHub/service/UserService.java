package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.UserCreationRequest;
import com.example.VolunteerHub.dto.response.UserCreationResponse;
import com.example.VolunteerHub.entity.UserRoles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.entity.key.UserRolesKey;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserRepository;
import com.example.VolunteerHub.repository.UserRolesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRolesRepository userRolesRepository;

    public UserCreationResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("user existed");

        Users user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .userRoles(new ArrayList<>())
                .isActive(true)
                .createdAt(Instant.now())
                .build();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        var role = roleRepository.findByRole(RoleEnum.VOLUNTEER);

        UserRolesKey key = new UserRolesKey(user.getId(), role.getId());

        UserRoles userRoles = UserRoles.builder()
                .id(key)
                .user(user)
                .role(role)
                .build();

        userRolesRepository.save(userRoles);

        user.getUserRoles().add(userRoles);

        return UserCreationResponse.builder()
                .success(true)
                .build();
    }

}

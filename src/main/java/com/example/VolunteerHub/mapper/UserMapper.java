package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.response.UserResponse;
import com.example.VolunteerHub.entity.Users;
import org.mapstruct.Mapper;

@Mapper
public class UserMapper {
    public static UserResponse toUserResponse(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getProfile().getFullName())
                .isActive(user.getProfile().isActive())
                .createdAt(user.getProfile().getCreatedAt())
                .build();
    }
}

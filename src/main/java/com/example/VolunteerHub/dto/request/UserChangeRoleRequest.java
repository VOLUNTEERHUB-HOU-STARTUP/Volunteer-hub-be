package com.example.VolunteerHub.dto.request;

import com.example.VolunteerHub.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangeRoleRequest {
    private UUID userId;
    private RoleEnum newRole;
}

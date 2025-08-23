package com.example.VolunteerHub.dto.request;

import com.example.VolunteerHub.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangeRoleRequest {
    private String email;
    private RoleEnum newRole;
}

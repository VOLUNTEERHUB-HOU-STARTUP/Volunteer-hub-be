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
public class UserCreationRequest {
    private String email;
    private String password;
    private String fullName;
    private RoleEnum role;
}

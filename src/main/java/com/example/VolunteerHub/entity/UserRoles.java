package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.entity.key.UserRolesKey;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoles {
    @EmbeddedId
    UserRolesKey id;

    @ManyToOne
    @MapsId("userId")  // ánh xạ vs userId trong key
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    Roles role;
}

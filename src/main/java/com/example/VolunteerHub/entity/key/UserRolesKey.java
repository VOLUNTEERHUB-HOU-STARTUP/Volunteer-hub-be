package com.example.VolunteerHub.entity.key;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class UserRolesKey implements Serializable {
    int userId;
    int roleId;
}

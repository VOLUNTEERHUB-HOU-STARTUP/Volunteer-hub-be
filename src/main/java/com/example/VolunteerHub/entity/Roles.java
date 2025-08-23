package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    RoleEnum role;

    // user
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Users> userRoles = new ArrayList<>();
}

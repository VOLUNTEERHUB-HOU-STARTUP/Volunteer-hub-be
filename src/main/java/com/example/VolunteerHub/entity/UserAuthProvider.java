package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.AuthProviderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    AuthProviderEnum provider;
    String providerId;  // email v fb
    String password; // set khi là email

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;
}

package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.UserAuthProvider;
import com.example.VolunteerHub.enums.AuthProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, UUID> {
    Optional<UserAuthProvider> findByProviderAndProviderId(AuthProviderEnum provider, String providerId);
}

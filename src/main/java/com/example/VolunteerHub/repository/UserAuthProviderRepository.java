package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.UserAuthProvider;
import com.example.VolunteerHub.enums.AuthProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//@Repository
public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, Integer> {
//    Optional<UserAuthProvider> findByProviderAndProviderId(AuthProviderEnum provider, int providerId);
}

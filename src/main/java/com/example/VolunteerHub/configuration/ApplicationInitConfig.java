package com.example.VolunteerHub.configuration;

import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.repository.ProfileRepository;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileRepository profileRepository;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            value = "driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@system.com").isEmpty()) {
                var role = roleRepository.findByRole(RoleEnum.ADMIN);

                Users user = Users.builder()
                        .email("admin@system.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(role)
                        .build();

                Profiles profile = Profiles.builder()
                        .user(user)
                        .fullName("admin.system")
                        .isActive(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                user.setProfile(profile);
                userRepository.save(user);

                log.warn("admin user has been created with default password: admin. " +
                        "u need change it with stronger password!");
            }
        };
    }
}

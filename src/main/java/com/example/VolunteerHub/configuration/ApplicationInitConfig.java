package com.example.VolunteerHub.configuration;

import com.example.VolunteerHub.entity.UserRoles;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.entity.key.UserRolesKey;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserRepository;
import com.example.VolunteerHub.repository.UserRolesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRolesRepository userRolesRepository;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@system.com").isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(RoleEnum.ADMIN.name());

                Users user = Users.builder()
                        .email("admin@system.com")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(user);

                var role = roleRepository.findByRole(RoleEnum.ADMIN);

                UserRolesKey key = new UserRolesKey(user.getId(), role.getId());

                UserRoles userRoles = UserRoles.builder()
                        .id(key)
                        .user(user)
                        .role(roleRepository.findByRole(RoleEnum.ADMIN))
                        .build();

                userRolesRepository.save(userRoles);

                user.getUserRoles().add(userRoles);
                log.warn("admin user has been created with default password: admin. " +
                        "u need change it with stronger password!");
            }
        };
    }
}

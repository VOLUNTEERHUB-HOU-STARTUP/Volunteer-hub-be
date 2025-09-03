package com.example.VolunteerHub.service;

import com.example.VolunteerHub.entity.Profiles;
import com.example.VolunteerHub.entity.UserAuthProvider;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.AuthProviderEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.repository.RoleRepository;
import com.example.VolunteerHub.repository.UserAuthProviderRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    UserRepository userRepository;
    UserAuthProviderRepository userAuthProviderRepository;
    RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("Google attributes: {}", oAuth2User.getAttributes());

        //provider(gg, fb)
        String providerName = userRequest.getClientRegistration().getRegistrationId();
        AuthProviderEnum provider = AuthProviderEnum.valueOf(providerName.toUpperCase());

        // lấy dl
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatar = oAuth2User.getAttribute("picture");

        //kiểm tra db
        Optional<UserAuthProvider> userAuth =
                userAuthProviderRepository.findByProviderAndProviderId(provider, providerId);

        if (userAuth.isEmpty()) {
            Profiles profile = Profiles.builder()
                    .fullName(name)
                    .avatarUrl(avatar)
                    .build();

            Users newUser = Users.builder()
                    .email(email)
                    .profile(profile)
                    .role(roleRepository.findByRole(RoleEnum.VOLUNTEER))
                    .build();

            profile.setUser(newUser);

            userRepository.save(newUser);

            UserAuthProvider userAuthProvider = UserAuthProvider.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .avatarUrl(avatar)
                    .user(newUser)
                    .build();

            userAuthProviderRepository.save(userAuthProvider);
        } else {
            UserAuthProvider userAuthProvider = userAuth.get();
            userAuthProvider.setAvatarUrl(avatar);

            userAuthProviderRepository.save(userAuthProvider);
        }

        return oAuth2User;
    }
}

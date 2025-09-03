package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.response.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class GoogleController {
    @GetMapping("/user")
    ApiResponse<?> getUser(@AuthenticationPrincipal OidcUser principal) {

        return ApiResponse.builder()
                .result(principal.getAttributes())
                .build();
    }
}

package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.AuthenticationRequest;
import com.example.VolunteerHub.dto.request.IntrospectRequest;
import com.example.VolunteerHub.dto.request.LogoutRequest;
import com.example.VolunteerHub.dto.request.RefreshRequest;
import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.dto.response.AuthenticationResponse;
import com.example.VolunteerHub.dto.response.IntrospectResponse;
import com.example.VolunteerHub.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResponse)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        IntrospectResponse introspectResponse = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(introspectResponse)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }
}

package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.UserCreationRequest;
import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.dto.response.UserCreationResponse;
import com.example.VolunteerHub.dto.response.UserResponse;
import com.example.VolunteerHub.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/create")
    ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserCreationResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("")
    ApiResponse<List<UserResponse>> getListUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getListUser())
                .build();
    }

}

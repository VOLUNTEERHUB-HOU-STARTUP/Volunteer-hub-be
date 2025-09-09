package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.dto.response.NotificationResponse;
import com.example.VolunteerHub.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/admin")
    ApiResponse<List<NotificationResponse>> getNotificationResponse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotificationResponse(page, size))
                .build();
    }
}

package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.ApplyEventRequest;
import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.service.EventVolunteerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event-volunteers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventVolunteerController {
    EventVolunteerService eventVolunteerService;

    @PostMapping("/apply")
    ApiResponse<Void> applyEvent(@RequestBody ApplyEventRequest request) {
        eventVolunteerService.applyEvent(request);

        return ApiResponse.<Void>builder().build();
    }
}

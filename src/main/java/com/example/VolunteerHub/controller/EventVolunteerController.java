package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.ApplyEventRequest;
import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.dto.response.EventVolunteerResponse;
import com.example.VolunteerHub.service.EventVolunteerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/get/{eventId}")
    ApiResponse<List<EventVolunteerResponse>> getListEventVolunteer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable UUID eventId
            ) {
        return ApiResponse.<List<EventVolunteerResponse>>builder()
                .result(eventVolunteerService.getListEventVolunteer(eventId, page, size))
                .build();
    }
}

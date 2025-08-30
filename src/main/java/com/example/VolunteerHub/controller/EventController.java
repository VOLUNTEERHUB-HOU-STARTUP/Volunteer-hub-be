package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.EventChangePublishedRequest;
import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {
    EventService eventService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<Void> createEvent(@ModelAttribute EventCreationRequest request) {
        eventService.createEvent(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("")
    ApiResponse<List<EventResponse>> getListEventWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventWithPaging(page, size))
                .build();
    }

    @GetMapping("/volunteer")
    ApiResponse<List<EventResponse>> getListEventWithVolunteerRole(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventWithVolunteerRole(page, size))
                .build();
    }

    @PostMapping("/change")
    ApiResponse<EventResponse> changePublished(@RequestBody EventChangePublishedRequest request) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.changePublished(request))
                .build();
    }
}

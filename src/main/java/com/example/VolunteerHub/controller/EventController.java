package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.response.ApiResponse;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {
    EventService eventService;

//    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    ApiResponse<Void> createEvent(@ModelAttribute EventCreationRequest request) {
//        eventService.createEvent(request);
//        return ApiResponse.<Void>builder()
//                .build();
//    }

    @GetMapping("/admin/get-all")
    ApiResponse<List<EventResponse>> getListEventWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventWithPaging(page, size))
                .build();
    }

    @GetMapping("/admin/get-unpublished")
    ApiResponse<List<EventResponse>> getListEventNotPublishedWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventNotPublishedWithPaging(page, size))
                .build();
    }

    @GetMapping("/admin/get-expired")
    ApiResponse<List<EventResponse>> getListEventHasExpired(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventExpired(page, size))
                .build();
    }

    @GetMapping("/admin/get-published")
    ApiResponse<List<EventResponse>> getListEventPublishedWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventPublishedWithPaging(page, size))
                .build();
    }

    @GetMapping("/user")
    ApiResponse<List<EventResponse>> getListEventWithoutAdminRole(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventWithoutAdminRole(page, size))
                .build();
    }

    @GetMapping("/user/get-expired")
    ApiResponse<List<EventResponse>> getListEventHasExpiredWithoutAdminRole(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventHasExpiredWithoutAdminRole(page, size))
                .build();
    }

    @GetMapping("/{eventId}")
    ApiResponse<EventResponse> getEventDetail(@PathVariable UUID eventId) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getEventDetail(eventId))
                .build();
    }

    @GetMapping("/{slug}")
    ApiResponse<EventResponse> getEventBySlug(@PathVariable String slug) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getEventBySlug(slug))
                .build();
    }

    @DeleteMapping("/delete/{eventId}")
    ApiResponse<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);

        return ApiResponse.<Void>builder().build();
    }
}

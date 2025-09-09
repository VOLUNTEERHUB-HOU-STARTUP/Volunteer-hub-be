package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.ServiceCreationRequest;
import com.example.VolunteerHub.dto.response.*;
import com.example.VolunteerHub.service.AdminDashboardService;
import com.example.VolunteerHub.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminDashboardController {
    AdminDashboardService adminDashboardService;
    EventService eventService;

//    @GetMapping("/dashboard-pendingQueue")
//    ApiResponse<PendingQueueResponse> getPendingQueueResponse() {
//        return ApiResponse.<PendingQueueResponse>builder()
//                .result(adminDashboardService.getPendingQueueResponse())
//                .build();
//    }

    // event management
    @GetMapping("/events")
    ApiResponse<List<EventResponse>> getListEventWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventWithPaging(page, size))
                .build();
    }

    @GetMapping("/events/unPublished")
    ApiResponse<List<EventResponse>> getListEventNotPublishedWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventNotPublishedWithPaging(page, size))
                .build();
    }

    @GetMapping("/events/expired")
    ApiResponse<List<EventResponse>> getListEventHasExpired(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventExpired(page, size))
                .build();
    }

    @GetMapping("/events/published")
    ApiResponse<List<EventResponse>> getListEventPublishedWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(eventService.getListEventPublishedWithPaging(page, size))
                .build();
    }

    @GetMapping("/event/{eventId}")
    ApiResponse<EventResponse> getEventDetail(@PathVariable UUID eventId) {
        return ApiResponse.<EventResponse>builder()
                .result(eventService.getEventDetail(eventId))
                .build();
    }

    @PutMapping("/event/{eventId}/approve")
    ApiResponse<Void> approveEvent(@PathVariable UUID eventId) {
        eventService.approveEvent(eventId);

        return ApiResponse.<Void>builder().build();
    }

    @PutMapping("/event/{eventId}/reject")
    ApiResponse<Void> rejectEvent(@PathVariable UUID eventId) {
        eventService.rejectEvent(eventId);

        return ApiResponse.<Void>builder().build();
    }

    // volunteer management
    @GetMapping("/volunteers")
    ApiResponse<List<ProfileResponse>> getListVolunteers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(adminDashboardService.getListVolunteer(page, size))
                .build();
    }

    @GetMapping("/volunteers/{userId}")
    ApiResponse<ProfileResponse> getDetailVolunteer(@PathVariable UUID userId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(adminDashboardService.getDetailVolunteer(userId))
                .build();
    }

    @PostMapping("/volunteers/{userId}/ban")
    ApiResponse<Void> banVolunteer(@PathVariable UUID userId) {
        adminDashboardService.banVolunteer(userId);

        return ApiResponse.<Void>builder().build();
    }

    // partner management
    @GetMapping("/organizers")
    ApiResponse<List<ProfileResponse>> getListOrganizer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(adminDashboardService.getListOrganizer(page, size))
                .build();
    }

    @GetMapping("/organizers/{userId}")
    ApiResponse<ProfileResponse> getDetailOrganizer(@PathVariable UUID userId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(adminDashboardService.getDetailOrganizer(userId))
                .build();
    }

    // service
    @GetMapping("/services")
    ApiResponse<List<ServiceResponse>> getListService(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ServiceResponse>>builder()
                .result(adminDashboardService.getListService(page, size))
                .build();
    }

    @PostMapping("/services")
    ApiResponse<Void> createService(@RequestBody ServiceCreationRequest request) {
        adminDashboardService.createService(request);

        return ApiResponse.<Void>builder()
                .build();
    }
}

package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.request.EventUpdateRequest;
import com.example.VolunteerHub.dto.request.ProfileUpdateRequest;
import com.example.VolunteerHub.dto.request.VolunteerRatingRequest;
import com.example.VolunteerHub.dto.response.*;
import com.example.VolunteerHub.service.OrganizerDashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/btc")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizerDashboardController {
    OrganizerDashboardService organizerDashboardService;

    //event
    @GetMapping("/events")
    ApiResponse<List<EventResponse>> getListEvent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(organizerDashboardService.getAllEvent(page, size))
                .build();
    }

    @GetMapping("/events/published")
    ApiResponse<List<EventResponse>> getListEventPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(organizerDashboardService.getListEventPublished(page, size))
                .build();
    }

    @GetMapping("/events/unPublished")
    ApiResponse<List<EventResponse>> getListEventUnPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventResponse>>builder()
                .result(organizerDashboardService.getListEventUnPublished(page, size))
                .build();
    }

    @GetMapping("/events/{eventId}")
    ApiResponse<EventResponse> getDetailEvent(@PathVariable UUID eventId) {
        return ApiResponse.<EventResponse>builder()
                .result(organizerDashboardService.getDetailEvent(eventId))
                .build();
    }

    @PostMapping("/events/create")
    ApiResponse<Void> createEvent(@RequestBody EventCreationRequest request) {
        organizerDashboardService.createEvent(request);
        return ApiResponse.<Void>builder().build();
    }

    @PatchMapping("/events/{slug}/update")
    ApiResponse<Void> updateEvent(@PathVariable String slug, @RequestBody EventUpdateRequest request) {
        organizerDashboardService.updateEvent(slug, request);

        return ApiResponse.<Void>builder().build();
    }

    //profile
    @GetMapping("/my-profile")
    ApiResponse<ProfileResponse> getMyProfile() {
        return ApiResponse.<ProfileResponse>builder()
                .result(organizerDashboardService.getMyProfile())
                .build();
    }

    // volunteer management
    @GetMapping("/{slug}/volunteers")
    ApiResponse<List<ProfileResponse>> getListVolunteerOfEvent(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(organizerDashboardService.getListVolunteerOfEvent(slug, page, size))
                .build();
    }

    @GetMapping("/volunteers/{volunteerId}")
    ApiResponse<ProfileResponse> getDetailVolunteer(@PathVariable UUID volunteerId) {
        return ApiResponse.<ProfileResponse>builder()
                .result(organizerDashboardService.getDetailVolunteer(volunteerId))
                .build();
    }

    @GetMapping("/volunteers/recommendations")
    ApiResponse<List<ProfileResponse>> getRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(organizerDashboardService.getRecommendations(page, size))
                .build();
    }

    // organizer profile
    @GetMapping("/profile/{organizerId}")
    ApiResponse<ProfileResponse> getDetailOrganizer() {
        return ApiResponse.<ProfileResponse>builder()
                .result(organizerDashboardService.getDetailOrganizer())
                .build();
    }

    @PatchMapping("/profile/update")
    ApiResponse<Void> updateOrganizerProfile(@RequestBody ProfileUpdateRequest request) {
        organizerDashboardService.updateProfile(request);

        return ApiResponse.<Void>builder().build();
    }

    // service
    @GetMapping("/services")
    ApiResponse<List<ServiceResponse>> getListService(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ServiceResponse>>builder()
                .result(organizerDashboardService.getListService(page, size))
                .build();
    }

    //event volunteer
    @GetMapping("/{slug}/volunteer/accepted")
    ApiResponse<List<EventVolunteerResponse>> getListAccepted(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventVolunteerResponse>>builder()
                .result(organizerDashboardService.getAcceptedVolunteersByEvent(slug, page, size))
                .build();
    }

    @GetMapping("/{slug}/volunteer/pending")
    ApiResponse<List<EventVolunteerResponse>> getListPending(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventVolunteerResponse>>builder()
                .result(organizerDashboardService.getPendingVolunteersByEvent(slug, page, size))
                .build();
    }

    @GetMapping("/{slug}/volunteer/rejected")
    ApiResponse<List<EventVolunteerResponse>> getListRejected(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<EventVolunteerResponse>>builder()
                .result(organizerDashboardService.getRejectedVolunteersByEvent(slug, page, size))
                .build();
    }

    @PostMapping("/{slug}/volunteer/handling/{userId}")
    ApiResponse<Void> handlingVolunteerRequest(
            @PathVariable String slug,
            @PathVariable UUID userId,
            @RequestParam boolean accept
    ) {
        organizerDashboardService.handlingVolunteerRequest(slug, userId, accept);

        return ApiResponse.<Void>builder().build();
    }

    // volunteer rating
    @PostMapping("/volunteer/{userId}/rating")
    ApiResponse<Void> ratingVolunteer(
            @PathVariable UUID userId,
            @RequestBody VolunteerRatingRequest request
    ) {
        organizerDashboardService.ratingVolunteer(userId, request.getStar());

        return ApiResponse.<Void>builder().build();
    }
}

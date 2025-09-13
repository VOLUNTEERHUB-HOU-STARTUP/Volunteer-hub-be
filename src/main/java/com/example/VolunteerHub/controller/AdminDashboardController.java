package com.example.VolunteerHub.controller;

import com.example.VolunteerHub.dto.request.*;
import com.example.VolunteerHub.dto.response.*;
import com.example.VolunteerHub.service.AdminDashboardService;
import com.example.VolunteerHub.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardController {
    AdminDashboardService adminDashboardService;
    EventService eventService;

//    @GetMapping("/dashboard-pendingQueue")
//    ApiResponse<PendingQueueResponse> getPendingQueueResponse() {
//        return ApiResponse.<PendingQueueResponse>builder()
//                .result(adminDashboardService.getPendingQueueResponse())
//                .build();
//    }

    @PostMapping("/events/{slug}/debug-parts")
    public ResponseEntity<String> debugParts(HttpServletRequest request) throws Exception {
        System.out.println("Content-Type = " + request.getContentType());
        try {
            Collection<Part> parts = request.getParts();
            System.out.println("parts count = " + (parts == null ? "null" : parts.size()));
            if (parts != null) {
                for (Part p : parts) {
                    System.out.println("part name=" + p.getName()
                            + " filename=" + p.getSubmittedFileName()
                            + " size=" + p.getSize());
                }
            }
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // event management
    @PostMapping(value = "/events/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<Void> createEvent(
            @RequestPart("event") String eventJson,
            @RequestPart(value = "listEventMedia", required = false) List<MultipartFile> listEventMedia,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        EventCreationRequest request = mapper.readValue(eventJson, EventCreationRequest.class);

        adminDashboardService.createEvent(request, coverImage, listEventMedia);

        return ApiResponse.<Void>builder().build();
    }

    @PostMapping(value = "/events/{slug}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<EventResponse> updateEvent(
            @RequestPart(value = "event") String eventJson,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
            @RequestPart(value = "listEventMedia", required = false) List<MultipartFile> listEventMedia,
            @PathVariable String slug
    ) throws JsonProcessingException {
        // gửi đúng thứ tự
//        System.out.println("coverImage = " + (coverImage != null ? coverImage : "null"));
//        System.out.println("listEventMedia = " + (listEventMedia != null ? listEventMedia.size() : "null"));
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        EventUpdateRequest request = mapper.readValue(eventJson, EventUpdateRequest.class);

        return ApiResponse.<EventResponse>builder()
                .result(adminDashboardService.updateEvent(slug, request, coverImage, listEventMedia))
                .build();
    }

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

    // Event category
    @GetMapping("/categories")
    ApiResponse<List<CategoryResponse>> getListCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(adminDashboardService.getListCategory())
                .build();
    }

    @GetMapping("/categories/{value}")
    ApiResponse<CategoryResponse> getDetailCategory(@PathVariable String value) {
        return ApiResponse.<CategoryResponse>builder()
                .result(adminDashboardService.getDetailCategory(value))
                .build();
    }

    @PostMapping("/categories/create")
    ApiResponse<Void> createCategory(@RequestBody CategoryCreationRequest request) {
        adminDashboardService.createCategory(request);

        return ApiResponse.<Void>builder()
                .build();
    }

    // event interest
    @GetMapping("/interests")
    ApiResponse<List<InterestResponse>> getListInterest() {
        return ApiResponse.<List<InterestResponse>>builder()
                .result(adminDashboardService.getListInterest())
                .build();
    }

    @GetMapping("/interests/{value}")
    ApiResponse<InterestResponse> getDetailInterest(@PathVariable String value) {
        return ApiResponse.<InterestResponse>builder()
                .result(adminDashboardService.getDetailInterest(value))
                .build();
    }

    @PostMapping("/interests/create")
    ApiResponse<Void> createInterest(@RequestBody InterestRequest request) {
        adminDashboardService.createInterest(request);

        return ApiResponse.<Void>builder().build();
    }

    // required skill
    @GetMapping("/required-skills")
    ApiResponse<List<RequiredSkillResponse>> getListRequiredSkill() {
        return ApiResponse.<List<RequiredSkillResponse>>builder()
                .result(adminDashboardService.getListRequiredSkill())
                .build();
    }

    @GetMapping("/required-skills/{value}")
    ApiResponse<RequiredSkillResponse> getDetailRRequiredSkill(@PathVariable String value) {
        return ApiResponse.<RequiredSkillResponse>builder()
                .result(adminDashboardService.getDetailRequiredSkill(value))
                .build();
    }

    @PostMapping("/required-skills/create")
    ApiResponse<Void> createRequiredSKill(@RequestBody RequiredSkillRequest request) {
        adminDashboardService.createRequiredSkill(request);

        return ApiResponse.<Void>builder().build();
    }

    // type tag
    @GetMapping("/type-tags")
    ApiResponse<List<TypeTagResponse>> getListTypeTag() {
        return ApiResponse.<List<TypeTagResponse>>builder()
                .result(adminDashboardService.getListTypeTag())
                .build();
    }

    @GetMapping("/type-tags/{value}")
    ApiResponse<TypeTagResponse> getDetailTypeTag(@PathVariable String value) {
        return ApiResponse.<TypeTagResponse>builder()
                .result(adminDashboardService.getDetailTypeTag(value))
                .build();
    }

    @PostMapping("/type-tags/create")
    ApiResponse<Void> createTypeTag(@RequestBody TypeTagRequest request) {
        adminDashboardService.createTypeTag(request);

        return ApiResponse.<Void>builder().build();
    }
}

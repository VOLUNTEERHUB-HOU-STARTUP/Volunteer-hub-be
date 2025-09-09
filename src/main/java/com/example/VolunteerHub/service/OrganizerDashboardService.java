package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.request.EventUpdateRequest;
import com.example.VolunteerHub.dto.request.ProfileUpdateRequest;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.dto.response.EventVolunteerResponse;
import com.example.VolunteerHub.dto.response.ProfileResponse;
import com.example.VolunteerHub.dto.response.ServiceResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizerDashboardService {
    EventService eventService;
    ProfileService profileService;
    EventVolunteerService eventVolunteerService;
    ServiceService serviceService;
    RecommendationService recommendationService;

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventResponse> getAllEvent(int page, int size) {
        return eventService.getListEventByUser(page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventResponse> getListEventPublished(int page, int size) {
        return eventService.getListEventPublishedByUser(page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventResponse> getListEventUnPublished(int page, int size) {
        return eventService.getListEventUnPublishedByUser(page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public EventResponse getDetailEvent(UUID eventId) {
        return eventService.getEventDetail(eventId);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public void createEvent(EventCreationRequest request) {
        eventService.createEvent(request);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public void updateEvent(String slug, EventUpdateRequest request) {
        eventService.updateEvent(slug, request);
    }
    // profile
    @PreAuthorize("hasRole('ORGANIZER')")
    public ProfileResponse getMyProfile() {
        return profileService.getMyInfo();
    }

    // volunteer profile
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<ProfileResponse> getListVolunteerOfEvent(String slug , int page, int size) {
        return profileService.getListVolunteerOfEvent(slug, page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public ProfileResponse getDetailVolunteer(UUID userId) {
        return profileService.getProfileWithUserId(userId);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<ProfileResponse> getRecommendations(int page, int size) {
        return recommendationService.getRecommendations(page, size);
    }

    // organizer profile
    @PreAuthorize("hasRole('ORGANIZER')")
    public ProfileResponse getDetailOrganizer() {
        return profileService.getMyInfo();
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public void updateProfile(ProfileUpdateRequest request) {
        profileService.updateProfile(request);
    }

    //service
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<ServiceResponse> getListService(int page, int size) {
        return serviceService.getListService(page, size);
    }

    // event volunteer
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventVolunteerResponse> getAcceptedVolunteersByEvent(String slug, int page, int size) {
        return eventVolunteerService.getAcceptedVolunteersByEvent(slug, page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventVolunteerResponse> getPendingVolunteersByEvent(String slug, int page, int size) {
        return eventVolunteerService.getPendingVolunteersByEvent(slug, page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventVolunteerResponse> getRejectedVolunteersByEvent(String slug, int page, int size) {
        return eventVolunteerService.getRejectedVolunteersByEvent(slug, page, size);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public void handlingVolunteerRequest(String slug, UUID userId, boolean accept) {
        eventVolunteerService.handlingVolunteerRequestBySlug(slug, userId, accept);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    public void ratingVolunteer(UUID userId, int star) {
        eventVolunteerService.ratingVolunteer(userId, star);
    }
}

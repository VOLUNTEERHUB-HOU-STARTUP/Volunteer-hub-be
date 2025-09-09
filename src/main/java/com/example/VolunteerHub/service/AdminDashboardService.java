package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ServiceCreationRequest;
import com.example.VolunteerHub.dto.response.*;
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
public class AdminDashboardService {
    ProfileService profileService;
    ServiceService serviceService;
    UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
//    public PendingQueueResponse getPendingQueueResponse() {
//
//    }

    // volunteer
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getListVolunteer(int page, int size) {
        return profileService.getListVolunteer(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProfileResponse getDetailVolunteer(UUID userId) {
        return profileService.getProfileWithUserId(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void banVolunteer(UUID userId) {
        userService.banUser(userId);
    }

    // organizer
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getListOrganizer(int page, int size) {
        return profileService.getListOrganizer(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProfileResponse getDetailOrganizer(UUID userId) {
        return profileService.getProfileWithUserId(userId);
    }

    // service
    @PreAuthorize("hasRole('ADMIN')")
    public List<ServiceResponse> getListService(int page, int size) {
        return serviceService.getListService(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createService(ServiceCreationRequest request) {
        serviceService.createService(request);
    }
}

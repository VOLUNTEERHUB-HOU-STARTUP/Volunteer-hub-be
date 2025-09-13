package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.*;
import com.example.VolunteerHub.dto.response.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardService {
    ProfileService profileService;
    ServiceService serviceService;
    UserService userService;
    CategoryService categoryService;
    InterestService interestService;
    RequiredSkillService requiredSkillService;
    TypeTagService typeTagService;
    EventService eventService;

//    @PreAuthorize("hasRole('ADMIN')")
//    public PendingQueueResponse getPendingQueueResponse() {
//
//    }
    //event
    @PreAuthorize("hasRole('ADMIN')")
    public void createEvent(EventCreationRequest request, MultipartFile coverImage, List<MultipartFile> listEventMedia) {
        eventService.createEvent(request, coverImage, listEventMedia);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse updateEvent(String slug, EventUpdateRequest request, MultipartFile coverImage, List<MultipartFile> listMediaFile) {
        return eventService.updateEvent(slug, request, coverImage, listMediaFile);
    }

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

    //event category
    public List<CategoryResponse> getListCategory() {
        return categoryService.getAll();
    }

    public CategoryResponse getDetailCategory(String value) {
        return categoryService.getDetail(value);
    }

    public void createCategory(CategoryCreationRequest request) {
        categoryService.createCategory(request);
    }

    // event interest
    public List<InterestResponse> getListInterest() {
        return interestService.getAll();
    }

    public InterestResponse getDetailInterest(String value) {
        return interestService.getDetail(value);
    }

    public void createInterest(InterestRequest request) {
        interestService.createInterest(request);
    }

    // required skill
    public List<RequiredSkillResponse> getListRequiredSkill() {
        return requiredSkillService.getAll();
    }

    public RequiredSkillResponse getDetailRequiredSkill(String value) {
        return requiredSkillService.getDetail(value);
    }

    public void createRequiredSkill(RequiredSkillRequest request) {
        requiredSkillService.createRequiredSkill(request);
    }

    // Type tag
    public List<TypeTagResponse> getListTypeTag() {
        return typeTagService.getAll();
    }

    public TypeTagResponse getDetailTypeTag(String value) {
        return typeTagService.getDetail(value);
    }

    public void createTypeTag(TypeTagRequest request) {
        typeTagService.createTypeTag(request);
    }
}

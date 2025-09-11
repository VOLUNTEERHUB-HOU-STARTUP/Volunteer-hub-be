package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ApplyEventRequest;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.dto.response.EventVolunteerResponse;
import com.example.VolunteerHub.dto.response.ProfileResponse;
import com.example.VolunteerHub.entity.*;
import com.example.VolunteerHub.enums.EventStatusEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.enums.VolunteerStatusEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.EventVolunteerMapper;
import com.example.VolunteerHub.mapper.ProfileMapper;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.EventVolunteerRepository;
import com.example.VolunteerHub.repository.UserRepository;
import com.example.VolunteerHub.repository.VolunteerRatingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventVolunteerService {
    EventVolunteerRepository eventVolunteerRepository;
    UserRepository userRepository;
    EventRepository eventRepository;
    VolunteerRatingRepository volunteerRatingRepository;
    VolunteerProfileService volunteerProfileService;
    EventService eventService;

    public void applyEvent(ApplyEventRequest request) {
         var context = SecurityContextHolder.getContext();
         String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Events event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        if (!(event.getStatus() == EventStatusEnum.IS_PUBLISHED))
            throw new AppException(ErrorCode.EVENT_NOT_PUBLISH);

        if (eventVolunteerRepository.checkExisted(user.getId(), event.getId()))
            throw new AppException(ErrorCode.EVENT_EXISTED);

        EventVolunteers eventVolunteer = EventVolunteers.builder()
                .user(user)
                .event(event)
                .createdAt(LocalDateTime.now())
                .isCheckedIn(false)
                .qrCode(null)
                .status(event.isAutoAccept() ? VolunteerStatusEnum.ACCEPTED : VolunteerStatusEnum.PENDING)
                .build();

        if (eventVolunteer.getStatus() == VolunteerStatusEnum.ACCEPTED)
            volunteerProfileService.updateTotalEventRegistered(user.getId());

        eventVolunteerRepository.save(eventVolunteer);
    }

    public List<EventVolunteerResponse> getListEventVolunteer(UUID eventId, int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAdmin = user.getRole().getRole() == RoleEnum.ADMIN;
        boolean isOwner = eventRepository.isEventOwner(user.getId(), eventId);

        if (!isAdmin && !isOwner)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Pageable pageable = PageRequest.of(page, size);

        Page<EventVolunteers> eventVolunteersPage = eventVolunteerRepository.findByEventId(eventId, pageable);

        return eventVolunteersPage.map(EventVolunteerMapper::toEventVolunteerResponse).toList();
    }

    public List<EventVolunteerResponse> getPendingEventsByUser(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);
        Page<EventVolunteers> pendingPage =
                eventVolunteerRepository.findPendingEventsByUser(user.getId(), pageable);

        return pendingPage.stream().map(EventVolunteerMapper::toEventVolunteerResponse).toList();
    }

    public List<EventVolunteerResponse> getAcceptedEventsByUser(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);
        Page<EventVolunteers> acceptedPage =
                eventVolunteerRepository.findAcceptedEventsByUser(user.getId(), pageable);

        return acceptedPage.stream().map(EventVolunteerMapper::toEventVolunteerResponse).toList();
    }

    public List<EventVolunteerResponse> getRejectedEventsByUser(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);
        Page<EventVolunteers> rejectedPage =
                eventVolunteerRepository.findRejectedEventsByUser(user.getId(), pageable);

        return rejectedPage.stream().map(EventVolunteerMapper::toEventVolunteerResponse).toList();
    }


    public void handlingVolunteerRequest(UUID eventId, UUID volunteerId, boolean accept) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAdmin = user.getRole().getRole() == RoleEnum.ADMIN;
        boolean isOwner = eventRepository.isEventOwner(user.getId(), eventId);

        if (!isAdmin && !isOwner)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (!eventVolunteerRepository.checkExisted(volunteerId, eventId))
            throw new AppException(ErrorCode.EVENT_VOLUNTEER_NOT_EXISTED);

        EventVolunteers ev = eventVolunteerRepository.findByEventIdAndVolunteerId(eventId, volunteerId);

        ev.setStatus(accept ? VolunteerStatusEnum.ACCEPTED : VolunteerStatusEnum.REJECTED);

        eventVolunteerRepository.save(ev);
    }

    public void handlingVolunteerRequestBySlug(String slug, UUID volunteerId, boolean accept) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAdmin = user.getRole().getRole() == RoleEnum.ADMIN;
        boolean isOwner = eventRepository.isEventOwnerBySlug(user.getId(), slug);

        if (!isAdmin && !isOwner)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (!eventVolunteerRepository.checkExistedBySlug(volunteerId, slug))
            throw new AppException(ErrorCode.EVENT_VOLUNTEER_NOT_EXISTED);

        EventResponse eventResponse = eventService.getEventBySlug(slug);

        EventVolunteers ev =
                eventVolunteerRepository.findByEventIdAndVolunteerId(eventResponse.getId(), volunteerId);

        ev.setStatus(accept ? VolunteerStatusEnum.ACCEPTED : VolunteerStatusEnum.REJECTED);

        if (ev.getStatus() == VolunteerStatusEnum.ACCEPTED)
            volunteerProfileService.updateTotalEventJoined(volunteerId);

        eventVolunteerRepository.save(ev);
    }

    public List<EventVolunteerResponse> getPendingVolunteersByEvent(String slug, int page, int size) {
        validateOwnerOrAdmin(slug);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventVolunteers> pendingPage =
                eventVolunteerRepository.findPendingVolunteersByEventId(slug, pageable);

        return pendingPage.stream()
                .map(EventVolunteerMapper::toEventVolunteerResponse)
                .toList();
    }

    public List<EventVolunteerResponse> getAcceptedVolunteersByEvent(String slug, int page, int size) {
        validateOwnerOrAdmin(slug);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventVolunteers> acceptedPage =
                eventVolunteerRepository.findAcceptedVolunteersByEventId(slug, pageable);

        return acceptedPage.stream()
                .map(EventVolunteerMapper::toEventVolunteerResponse)
                .toList();
    }

    public List<EventVolunteerResponse> getRejectedVolunteersByEvent(String slug, int page, int size) {
        validateOwnerOrAdmin(slug);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventVolunteers> rejectedPage =
                eventVolunteerRepository.findRejectedVolunteersByEventId(slug, pageable);

        return rejectedPage.stream()
                .map(EventVolunteerMapper::toEventVolunteerResponse)
                .toList();
    }

    // ktra quyền
    private void validateOwnerOrAdmin(String slug) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAdmin = user.getRole().getRole() == RoleEnum.ADMIN;
        boolean isOwner = eventRepository.isEventOwnerBySlug(user.getId(), slug);

        if (!isAdmin && !isOwner)
            throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    public void ratingVolunteer(UUID userId, int star) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (volunteerRatingRepository.existsByVolunteerIdAndOrganizerId(userId, user.getId()))
            throw new AppException(ErrorCode.RATING_EXISTED);

        Users volunteer = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        VolunteerRating volunteerRating = VolunteerRating.builder()
                .volunteer(volunteer)
                .organizer(user)
                .rating(star)
                .createdAt(LocalDateTime.now())
                .build();

        volunteerRatingRepository.save(volunteerRating);
    }
}

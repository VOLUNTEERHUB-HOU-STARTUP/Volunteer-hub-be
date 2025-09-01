package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ApplyEventRequest;
import com.example.VolunteerHub.dto.response.EventVolunteerResponse;
import com.example.VolunteerHub.entity.EventVolunteers;
import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.enums.VolunteerStatusEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.EventVolunteerRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void applyEvent(ApplyEventRequest request) {
         var context = SecurityContextHolder.getContext();
         String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Events event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        if (!event.isPublished())
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

        return eventVolunteersPage.map(ev -> EventVolunteerResponse.builder()
                .id(ev.getId())
                .userId(ev.getUser().getId())
                .avatarUrl(ev.getUser().getProfile().getAvatarUrl())
                .fullName(ev.getUser().getProfile().getFullName())
                .eventId(ev.getEvent().getId())
                .isCheckedIn(ev.isCheckedIn())
                .status(ev.getStatus())
                .checkInTime(ev.getCheckInTime())
                .createdAt(ev.getCreatedAt())
                .build()).toList();
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
}

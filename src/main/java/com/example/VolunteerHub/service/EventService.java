package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.EventChangePublishedRequest;
import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
    EventRepository eventRepository;
    UserRepository userRepository;

    public void createEvent(EventCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var role = user.getRole().getRole();
        if (role == RoleEnum.VOLUNTEER)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Events event = Events.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .salary(request.getSalary())
                .location(request.getLocation())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .isPublished(false)
                .createdAt(LocalDateTime.now())
                .build();

        eventRepository.save(event);
    }

    public List<EventResponse> getListEvent() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Events> eventList = eventRepository.findAll();

        return eventList.stream()
                .filter(event ->
                        user.getRole().getRole() == RoleEnum.ADMIN || event.isPublished()
                )
                .map(event -> EventResponse.builder()
                        .id(event.getId())
                        .userId(event.getUser().getId())
                        .fullName(event.getUser().getProfile().getFullName())
                        .avatarUrl(event.getUser().getProfile().getAvatarUrl())
                        .title(event.getTitle())
                        .description(event.getDescription())
                        .slug(event.getSlug())
                        .salary(event.getSalary())
                        .location(event.getLocation())
                        .isPublished(event.isPublished())
                        .maxVolunteer(event.getMaxVolunteer())
                        .startAt(event.getStartAt())
                        .endAt(event.getEndAt())
                        .build())
                .toList();
    }

    public EventResponse changePublished(EventChangePublishedRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var role = user.getRole().getRole();

        if (role != RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Events event = eventRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        event.setPublished(!event.isPublished());

        event = eventRepository.save(event);

        return EventResponse.builder()
                .id(event.getId())
                .userId(event.getUser().getId())
                .fullName(event.getUser().getProfile().getFullName())
                .avatarUrl(event.getUser().getProfile().getAvatarUrl())
                .title(event.getTitle())
                .description(event.getDescription())
                .slug(event.getSlug())
                .salary(event.getSalary())
                .location(event.getLocation())
                .isPublished(event.isPublished())
                .maxVolunteer(event.getMaxVolunteer())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .build();
    }
}
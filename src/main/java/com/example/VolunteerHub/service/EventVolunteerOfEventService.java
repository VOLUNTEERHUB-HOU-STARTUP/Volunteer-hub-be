package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.response.EventVolunteerResponse;
import com.example.VolunteerHub.entity.EventVolunteers;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.EventVolunteerMapper;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.EventVolunteerRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventVolunteerOfEventService {
    UserRepository userRepository;
    EventRepository eventRepository;
    EventVolunteerRepository eventVolunteerRepository;

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
}

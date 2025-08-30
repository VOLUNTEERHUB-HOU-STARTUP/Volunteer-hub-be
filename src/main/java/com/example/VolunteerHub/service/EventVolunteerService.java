package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ApplyEventRequest;
import com.example.VolunteerHub.entity.EventVolunteers;
import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.EventVolunteerRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        EventVolunteers eventVolunteer = EventVolunteers.builder()
                .user(user)
                .event(event)
                .createdAt(LocalDateTime.now())
                .isCheckedIn(false)
                .qrCode(null)
                .build();

        if (eventVolunteerRepository.checkExisted(user.getId(), event.getId())) {
            throw new AppException(ErrorCode.EVENT_EXISTED);
        }

        eventVolunteerRepository.save(eventVolunteer);
    }
}

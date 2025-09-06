package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.EventChangePublishedRequest;
import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.response.EventMediaResponse;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.entity.EventMedias;
import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.enums.MediaTypeEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.EventMapper;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.UserRepository;
import com.example.VolunteerHub.utils.AuthUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
    EventRepository eventRepository;
    CloudinaryService cloudinaryService;
    AuthUtil authUtil;

    public void createEvent(EventCreationRequest request) {
        Users user = authUtil.getCurrentUser();

        var role = user.getRole().getRole();
        if (role == RoleEnum.VOLUNTEER)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        boolean isExistedEventTitle = eventRepository.isExistedByEventTitle(request.getTitle());

        if (isExistedEventTitle)
            throw new AppException(ErrorCode.EVENT_EXISTED);

        Events event = Events.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .salary(request.getSalary())
                .location(request.getLocation())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .isPublished(false)
                .deadline(request.getDeadline())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        for (MultipartFile thisFile : request.getListEventMedia()) {
            if (!thisFile.isEmpty()) {
                Map<String, String> file;

                try {
                    file = cloudinaryService.uploadFile(thisFile);
                } catch (IOException e) {
                    throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
                }

                String fileUrl = file.get("url");
                String fileType = file.get("type");

                EventMedias media = EventMedias.builder()
                        .mediaType(fileType.equals("VIDEO") ? MediaTypeEnum.VIDEO : MediaTypeEnum.IMAGE)
                        .mediaUrl(fileUrl)
                        .event(event)
                        .build();

                event.getEventMedia().add(media);
            }
        }

        eventRepository.save(event);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .map(EventMapper::mapToResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventNotPublishedWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .filter(event ->!event.isPublished())
                .map(EventMapper::mapToResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventWaitingWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .filter(event -> !event.isPublished() && LocalDateTime.now().isAfter(event.getEndAt()))
                .map(EventMapper::mapToResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventExpired(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .filter(event -> LocalDateTime.now().isAfter(event.getEndAt()))
                .map(EventMapper::mapToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventPublishedWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .filter(event ->
                        LocalDateTime.now().isBefore(event.getEndAt()) &&
                        event.isPublished()
                )
                .map(EventMapper::mapToResponse)
                .toList();
    }

    public List<EventResponse> getListEventWithVolunteerRole(int page, int size) {
        Users user = authUtil.getCurrentUser();

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.findPublishedActiveEvents(LocalDateTime.now(), pageable);

        return eventList.stream()
                .filter(event ->
                        event.isPublished() &&
                        LocalDateTime.now().isBefore(event.getEndAt())
                )
                .map(EventMapper::mapToResponse)
                .toList();
    }

    public List<EventResponse> getListEventHasExpiredWithVolunteerRole(int page, int size) {
        Users user = authUtil.getCurrentUser();

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .filter(event ->
                                event.isPublished() &&
                                LocalDateTime.now().isAfter(event.getEndAt())
                )
                .map(EventMapper::mapToResponse)
                .toList();
    }

    public EventResponse getEventDetail(UUID eventId) {
        Users user = authUtil.getCurrentUser();

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        if (!event.isPublished() &&
                (user.getRole().getRole() != RoleEnum.ADMIN &&
                        !eventRepository.isEventOwner(user.getId(), event.getId())))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return EventMapper.mapToResponse(event);
    }

    public EventResponse changePublished(EventChangePublishedRequest request) {
        Users user = authUtil.getCurrentUser();

        var role = user.getRole().getRole();

        if (role != RoleEnum.ADMIN)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Events event = eventRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        event.setPublished(!event.isPublished());

        event = eventRepository.save(event);

        return EventMapper.mapToResponse(event);
    }

    public void deleteEvent(UUID eventId) {
        Users user = authUtil.getCurrentUser();

        var role = user.getRole().getRole();

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        if (role == RoleEnum.ADMIN || eventRepository.isEventOwner(user.getId(), event.getId()))
            eventRepository.delete(event);
    }
}
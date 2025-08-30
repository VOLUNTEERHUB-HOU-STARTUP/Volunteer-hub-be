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
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;

    public void createEvent(EventCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

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

                EventMedias.builder()
                        .mediaType(fileType.equals("VIDEO") ? MediaTypeEnum.VIDEO : MediaTypeEnum.IMAGE)
                        .mediaUrl(fileUrl)
                        .build();
            }
        }

        eventRepository.save(event);
    }

    public List<EventResponse> getListEventWithPaging(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

        return eventList.stream()
                .filter(event ->
                        LocalDateTime.now().isBefore(event.getEndAt()) &&
                        (user.getRole().getRole() == RoleEnum.ADMIN || event.isPublished())
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
                        .eventMedia(event.getEventMedia().stream().map(media ->
                                EventMediaResponse.builder()
                                        .id(media.getId())
                                        .mediaType(media.getMediaType())
                                        .mediaUrl(media.getMediaUrl())
                                        .build()).toList())
                        .startAt(event.getStartAt())
                        .endAt(event.getEndAt())
                        .deadline(event.getDeadline())
                        .createdAt(event.getCreatedAt())
                        .updatedAt(event.getUpdatedAt())
                        .build())
                .toList();
    }

    public List<EventResponse> getListEventWithVolunteerRole(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.getEventWithPaging(pageable);

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
                        .eventMedia(event.getEventMedia().stream().map(media ->
                                EventMediaResponse.builder()
                                        .id(media.getId())
                                        .mediaType(media.getMediaType())
                                        .mediaUrl(media.getMediaUrl())
                                        .build()).toList())
                        .startAt(event.getStartAt())
                        .endAt(event.getEndAt())
                        .deadline(event.getDeadline())
                        .createdAt(event.getCreatedAt())
                        .updatedAt(event.getUpdatedAt())
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
                .eventMedia(event.getEventMedia().stream().map(media ->
                        EventMediaResponse.builder()
                                .id(media.getId())
                                .mediaType(media.getMediaType())
                                .mediaUrl(media.getMediaUrl())
                                .build()).toList())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .deadline(event.getDeadline())
                .updatedAt(event.getUpdatedAt())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
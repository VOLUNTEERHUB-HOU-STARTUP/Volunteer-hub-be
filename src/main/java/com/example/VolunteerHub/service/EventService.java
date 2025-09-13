package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.request.EventUpdateRequest;
import com.example.VolunteerHub.dto.response.EventResponse;
import com.example.VolunteerHub.entity.*;
import com.example.VolunteerHub.enums.EventStatusEnum;
import com.example.VolunteerHub.enums.MediaTypeEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.EventMapper;
import com.example.VolunteerHub.repository.EventRepository;
import com.example.VolunteerHub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    UserRepository userRepository;
    CategoryService categoryService;
    RequiredSkillService requiredSkillService;
    TypeTagService typeTagService;
    InterestService interestService;

    public EventResponse createEvent(EventCreationRequest request, MultipartFile coverImage, List<MultipartFile> listEventMedia) {
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
        // các trường khác

        Events event = EventMapper.mapToEntity(request, user, request.isDraft());

        // Category
        if (request.getCategories() != null) {
            List<EventCategory> categories = request.getCategories().stream()
                    .map(cat -> EventCategory.builder()
                            .event(event)
                            .category(categoryService.getCategoryByString(cat))
                            .build())
                    .toList();
            event.setEventCategories(categories);
        }

        // Required Skills
        if (request.getSkills() != null) {
            List<EventRequiredSkill> skills = request.getSkills().stream()
                    .map(skill -> EventRequiredSkill.builder()
                            .event(event)
                            .requiredSkill(requiredSkillService.getRequiredSkillByString(skill))
                            .build())
                    .toList();

            event.setEventRequiredSkills(skills);
        }

        // Tags
        if (request.getTags() != null) {
            List<EventTypeTag> tags = request.getTags().stream()
                    .map(tag -> EventTypeTag.builder()
                            .event(event)
                            .typeTag(typeTagService.getTypeTagByString(tag))
                            .build()
            ).toList();
            event.setEventTypeTags(tags);
        }

        // Interest
        if (request.getInterests() != null) {
            List<EventInterest> interests = request.getInterests().stream()
                    .map(interest -> EventInterest.builder()
                            .event(event)
                            .interest(interestService.getInterestByString(interest))
                            .build())
                    .toList();
            event.setEventInterests(interests);
        }

        List<EventMedias> eventMediasList = new ArrayList<>();
        String coverUrl = null;

        if (coverImage != null && !coverImage.isEmpty()) {
            log.info("có ảnh");
            Map<String, String> file;

            try {
                file = cloudinaryService.uploadFile(coverImage);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }

            coverUrl = file.get("url");
        }

        event.setCoverImage(coverUrl);

        if (listEventMedia != null && !listEventMedia.isEmpty()) {
            for (MultipartFile thisFile : listEventMedia) {
                if (thisFile != null && !thisFile.isEmpty()) {
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

                    eventMediasList.add(media);
                }
            }
        }
        event.setEventMedia(eventMediasList);

        eventRepository.save(event);

        return EventMapper.mapToResponse(event);
    }

    public EventResponse updateEvent(String slug, EventUpdateRequest request, MultipartFile coverImage, List<MultipartFile> listMediaFile) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Events event = eventRepository.findBySlug(slug);

        if (event == null)
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);

        if (!eventRepository.isEventOwner(user.getId(), event.getId()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (request.getListDeleteMediaId() != null && !request.getListDeleteMediaId().isEmpty()) {
            event.getEventMedia().removeIf(eventMedia ->
                    request.getListDeleteMediaId().contains(eventMedia.getId()));
        }

        List<EventMedias> eventMediasList = new ArrayList<>();
        String coverUrl = null;

        if (coverImage != null && !coverImage.isEmpty()) {
            Map<String, String> file;

            try {
                file = cloudinaryService.uploadFile(coverImage);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
            }

            coverUrl = file.get("url");
        }
        event.setCoverImage(coverUrl);

        if (listMediaFile != null && !listMediaFile.isEmpty()) {
            for (MultipartFile thisFile : listMediaFile) {
                if (thisFile != null && !thisFile.isEmpty()) {
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

                    eventMediasList.add(media);
                }
            }
        }
        if (listMediaFile != null && !listMediaFile.isEmpty()) {
            for (EventMedias media : eventMediasList) {
                media.setEvent(event);
                event.getEventMedia().add(media);
            }
        }

        // Update categories
        if (request.getCategories() != null) {
            List<EventCategory> categories = request.getCategories().stream()
                    .map(cat -> EventCategory.builder()
                            .event(event)
                            .category(categoryService.getCategoryByString(cat))
                            .build())
                    .toList();
            event.setEventCategories(categories);
        }

        // Update skills
        if (request.getSkills() != null) {
            List<EventRequiredSkill> skills = request.getSkills().stream()
                    .map(skill -> EventRequiredSkill.builder()
                            .event(event)
                            .requiredSkill(requiredSkillService.getRequiredSkillByString(skill))
                            .build())
                    .toList();
            event.setEventRequiredSkills(skills);
        }

        // Update tags
        if (request.getTags() != null) {
            List<EventTypeTag> tags = request.getTags().stream()
                    .map(tag -> EventTypeTag.builder()
                            .event(event)
                            .typeTag(typeTagService.getTypeTagByString(tag))
                            .build())
                    .toList();
            event.setEventTypeTags(tags);
        }

        // Update interests
        if (request.getInterests() != null) {
            List<EventInterest> interests = request.getInterests().stream()
                    .map(interest -> EventInterest.builder()
                            .event(event)
                            .interest(interestService.getInterestByString(interest))
                            .build())
                    .toList();
            event.setEventInterests(interests);
        }

        EventStatusEnum oldStatus = event.getStatus();
        EventStatusEnum newStatus =
                request.isDraft()
                ? EventStatusEnum.DRAFT
                : user.getRole().getRole() == RoleEnum.ADMIN
                        ? EventStatusEnum.IS_PUBLISHED
                        : EventStatusEnum.PENDING;

        event.setStatus(newStatus);
        event.getSchedule().setUpdatedAt(LocalDateTime.now());

        if (oldStatus == EventStatusEnum.DRAFT && newStatus == EventStatusEnum.IS_PUBLISHED) {
            event.getSchedule().setCreatedAt(LocalDateTime.now());
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            event.setTitle(request.getTitle());
        }

        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            event.setLocation(request.getLocation());
        }

        if (request.getDetailLocation() != null && !request.getDetailLocation().isBlank()) {
            event.setDetailLocation(request.getDetailLocation());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            event.setDescription(request.getDescription());
        }

        if (request.getSalary() != null) {
            event.setSalary(request.getSalary());
        }

        if (request.getPriority() != null) {
            event.setPriority(request.getPriority());
        }

        if (request.getType() != null) {
            event.setType(request.getType());
        }

        if (request.getStartAt() != null) {
            event.getSchedule().setStartAt(request.getStartAt());
        }
        if (request.getEndAt() != null) {
            event.getSchedule().setEndAt(request.getEndAt());
        }
        if (request.getDeadline() != null) {
            event.getSchedule().setDeadline(request.getDeadline());
        }

        // Requirements
        if (request.getMaxVolunteer() != null) {
            event.getRequirements().setMaxVolunteer(request.getMaxVolunteer());
        }
        if (request.getMinAge() != null) {
            event.getRequirements().setMinAge(request.getMinAge());
        }
        if (request.getMaxAge() != null) {
            event.getRequirements().setMaxAge(request.getMaxAge());
        }
        if (request.getSex() != null) {
            event.getRequirements().setSex(request.getSex());
        }
        if (request.getExperience() != null) {
            event.getRequirements().setExperience(request.getExperience());
        }

        // Leader
        if (request.getLeaderName() != null && !request.getLeaderName().isBlank()) {
            event.getLeader().setLeaderName(request.getLeaderName());
        }
        if (request.getLeaderPhone() != null && !request.getLeaderPhone().isBlank()) {
            event.getLeader().setLeaderPhone(request.getLeaderPhone());
        }
        if (request.getLeaderEmail() != null && !request.getLeaderEmail().isBlank()) {
            event.getLeader().setLeader_email(request.getLeaderEmail());
        }
        if (request.getSubContact() != null && !request.getSubContact().isBlank()) {
            event.getLeader().setSubContact(request.getSubContact());
        }

        if (request.getOnline() != null) {
            event.setOnline(request.getOnline());
        }

        if (request.getAutoAccept() != null) {
            event.setAutoAccept(request.getAutoAccept());
        }

//        Events eventResponse = eventRepository.save(event);

        return EventMapper.mapToResponse(event);
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

        Page<Events> eventList = eventRepository.findByStatus(EventStatusEnum.PENDING, pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventExpired(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList =
                eventRepository.findExpiredEvents(LocalDateTime.now(), EventStatusEnum.IS_PUBLISHED, pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<EventResponse> getListEventPublishedWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.findByStatus(EventStatusEnum.IS_PUBLISHED, pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    public List<EventResponse> getListEventByUser(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList = eventRepository.findEventByUserId(user.getId(), pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    public List<EventResponse> getListEventPublishedByUser(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList =
                eventRepository.findPublishedEventsByUserId(user.getId(), EventStatusEnum.IS_PUBLISHED, pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    public List<EventResponse> getListEventUnPublishedByUser(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList =
                eventRepository.findUnpublishedEventsByUserId(user.getId(), EventStatusEnum.IS_PUBLISHED, pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    public List<EventResponse> getListEventWithoutAdminRole(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList =
                eventRepository.findPublishedActiveEvents(LocalDateTime.now(), EventStatusEnum.IS_PUBLISHED, pageable);

        return eventList.stream().map(EventMapper::mapToResponse).toList();
    }

    public List<EventResponse> getListEventHasExpiredWithoutAdminRole(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Events> eventList =
                eventRepository.findExpiredEvents(LocalDateTime.now(), EventStatusEnum.IS_PUBLISHED, pageable);

        return eventList.stream()
                .filter(event -> LocalDateTime.now().isAfter(event.getSchedule().getEndAt()))
                .map(EventMapper::mapToResponse)
                .toList();
    }

    public EventResponse getEventDetail(UUID eventId) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        boolean isPublished = event.getStatus() == EventStatusEnum.IS_PUBLISHED;
        boolean isOwner = eventRepository.isEventOwner(user.getId(), event.getId());
        boolean isAdmin = user.getRole().getRole() == RoleEnum.ADMIN;

        if (!isPublished && !isAdmin && !isOwner)
            throw new AppException(ErrorCode.UNAUTHORIZED);


        return EventMapper.mapToResponse(event);
    }

    public EventResponse getEventBySlug(String slug) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Events event = eventRepository.findBySlug(slug);

        if (event == null)
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);

        boolean isPublished = event.getStatus() == EventStatusEnum.IS_PUBLISHED;
        boolean isOwner = eventRepository.isEventOwner(user.getId(), event.getId());
        boolean isAdmin = user.getRole().getRole() == RoleEnum.ADMIN;

        if (!isPublished && !isAdmin && !isOwner)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return EventMapper.mapToResponse(event);
    }

    public void deleteEvent(UUID eventId) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var role = user.getRole().getRole();

        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        if (role == RoleEnum.ADMIN || eventRepository.isEventOwner(user.getId(), event.getId()))
            eventRepository.delete(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void approveEvent(UUID eventId) {
        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        if (event.getStatus() != EventStatusEnum.PENDING)
            throw new AppException(ErrorCode.EVENT_NOT_PENDING);

        event.getSchedule().setCreatedAt(LocalDateTime.now());
        event.setStatus(EventStatusEnum.IS_PUBLISHED);
        event.getSchedule().setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void rejectEvent(UUID eventId) {
        Events event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));

        event.setStatus(EventStatusEnum.REJECTED);

        eventRepository.save(event);
    }
}
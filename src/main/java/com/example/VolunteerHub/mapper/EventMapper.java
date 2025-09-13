package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.EventCreationRequest;
import com.example.VolunteerHub.dto.response.*;
import com.example.VolunteerHub.entity.*;
import com.example.VolunteerHub.enums.EventStatusEnum;
import com.example.VolunteerHub.enums.RoleEnum;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public class EventMapper {
    public static Events mapToEntity(EventCreationRequest request, Users user, boolean isDraft) {
        EventStatusEnum status = isDraft
                ? EventStatusEnum.DRAFT
                : user.getRole().getRole() == RoleEnum.ADMIN
                ? EventStatusEnum.IS_PUBLISHED
                : EventStatusEnum.PENDING;

        Events event = Events.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .detailLocation(request.getDetailLocation())
                .online(request.isOnline())
                .priority(request.getPriority())
                .type(request.getType())
                .autoAccept(request.isAutoAccept())
                .salary(request.getSalary())
                .status(status)
                .build();

        // schedule
        EventSchedule schedule = EventSchedule.builder()
                .event(event)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .deadline(request.getDeadline())
                .createdAt(status == EventStatusEnum.IS_PUBLISHED ? LocalDateTime.now() : null)
                .updatedAt(LocalDateTime.now())
                .build();
        event.setSchedule(schedule);

        // requirement
        EventRequirements requirement = EventRequirements.builder()
                .event(event)
                .maxVolunteer(request.getMaxVolunteer())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .sex(request.getSex())
                .experience(request.getExperience())
                .build();
        event.setRequirements(requirement);

        // leader
        EventLeader leader = EventLeader.builder()
                .event(event)
                .leaderName(request.getLeaderName())
                .leaderPhone(request.getLeaderPhone())
                .leader_email(request.getLeaderEmail())
                .subContact(request.getSubContact())
                .build();
        event.setLeader(leader);

        return event;
    }

    public static EventResponse mapToResponse(Events event) {
        if (event == null) return null;

        // categories
        var categoryList = event.getEventCategories() == null
                ? List.<CategoryResponse>of()
                : event.getEventCategories().stream().map(ec -> {
                    var category = ec.getCategory();

                    return CategoryResponse.builder()
                            .id(category.getId())
                            .value(category.getValue())
                            .label(category.getLabel())
                            .build();
                }).toList();

        // required skills
        var skillList = event.getEventRequiredSkills() == null
                ? List.<RequiredSkillResponse>of()
                : event.getEventRequiredSkills().stream().map(rs -> {
                    var skill = rs.getRequiredSkill();

                    return RequiredSkillResponse.builder()
                            .id(skill.getId())
                            .value(skill.getValue())
                            .label(skill.getLabel())
                            .build();
                }).toList();

        // tags
        var tagList = event.getEventTypeTags() == null
                ? List.<TypeTagResponse>of()
                : event.getEventTypeTags().stream()
                .map(t -> {
                    var typeTag = t.getTypeTag();

                    return TypeTagResponse.builder()
                            .id(typeTag.getId())
                            .value(typeTag.getValue())
                            .label(typeTag.getLabel())
                            .build();
                }).toList();

        // interests
        var interestList = event.getEventInterests() == null
                ? List.<InterestResponse>of()
                : event.getEventInterests().stream().map(i -> {
                    var interest = i.getInterest();

                    return InterestResponse.builder()
                            .id(interest.getId())
                            .value(interest.getValue())
                            .label(interest.getLabel())
                            .build();
                }).toList();

        return EventResponse.builder()
                .id(event.getId())
                .userId(event.getUser().getId())
                .fullName(event.getUser().getProfile().getFullName())
                .avatarUrl(event.getUser().getProfile().getAvatarUrl())
                .title(event.getTitle())
                .slug(event.getSlug())
                .description(event.getDescription())
                .salary(event.getSalary())
                .location(event.getLocation())
                .detailLocation(event.getDetailLocation())
                .startAt(event.getSchedule().getStartAt())
                .endAt(event.getSchedule().getEndAt())
                .deadline(event.getSchedule().getDeadline())
                .autoAccept(event.isAutoAccept())
                .type(event.getType())
                .minAge(event.getRequirements().getMinAge())
                .maxAge(event.getRequirements().getMaxAge())
                .sex(event.getRequirements().getSex())
                .experience(event.getRequirements().getExperience())
                .maxVolunteer(event.getRequirements().getMaxVolunteer())
                .leaderName(event.getLeader().getLeaderName())
                .leaderPhone(event.getLeader().getLeaderPhone())
                .leaderEmail(event.getLeader().getLeader_email())
                .subContact(event.getLeader().getSubContact())
                .priority(event.getPriority())
                .online(event.isOnline())
                .coverImage(event.getCoverImage())
                .status(event.getStatus())
                .interests(interestList)
                .categories(categoryList)
                .skills(skillList)
                .tags(tagList)
                .eventMedia(event.getEventMedia().stream().map(media ->
                        EventMediaResponse.builder()
                                .id(media.getId())
                                .mediaType(media.getMediaType())
                                .mediaUrl(media.getMediaUrl())
                                .build()).toList()
                )
                // like, comment
                .totalLike(event.getEventLikes() == null ? 0 : event.getEventLikes().size())
                .totalComment(event.getEventComments() == null ? 0 : event.getEventComments().size())
                .updatedAt(event.getSchedule().getUpdatedAt())
                .createdAt(event.getSchedule().getCreatedAt())
                .build();
    }
}

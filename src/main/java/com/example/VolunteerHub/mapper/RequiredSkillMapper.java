package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.RequiredSkillRequest;
import com.example.VolunteerHub.dto.response.RequiredSkillResponse;
import com.example.VolunteerHub.entity.RequiredSkills;
import org.mapstruct.Mapper;

@Mapper
public class RequiredSkillMapper {
    public static RequiredSkills toRequiredSkill(RequiredSkillRequest request) {
        return RequiredSkills.builder()
                .value(request.getValue())
                .label(request.getLabel())
                .build();
    }

    public static RequiredSkillResponse toResponse(RequiredSkills data) {
        return RequiredSkillResponse.builder()
                .value(data.getValue())
                .label(data.getLabel())
                .build();
    }
}

package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.TypeTagRequest;
import com.example.VolunteerHub.dto.response.TypeTagResponse;
import com.example.VolunteerHub.entity.TypeTags;
import org.mapstruct.Mapper;

@Mapper
public class TypeTagMapper {
    public static TypeTags toTypeTag(TypeTagRequest request) {
        return TypeTags.builder()
                .value(request.getValue())
                .label(request.getLabel())
                .build();
    }

    public static TypeTagResponse toResponse(TypeTags typeTag) {
        return TypeTagResponse.builder()
                .value(typeTag.getValue())
                .label(typeTag.getLabel())
                .build();
    }
}

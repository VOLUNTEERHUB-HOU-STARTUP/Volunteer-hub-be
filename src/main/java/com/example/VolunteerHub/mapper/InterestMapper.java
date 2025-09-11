package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.InterestRequest;
import com.example.VolunteerHub.dto.response.InterestResponse;
import com.example.VolunteerHub.entity.Interests;
import org.mapstruct.Mapper;

@Mapper
public class InterestMapper {
    public static Interests toCInterests(InterestRequest request) {
        return Interests.builder()
                .value(request.getValue())
                .label(request.getLabel())
                .build();
    }

    public static InterestResponse toResponse(Interests data) {
        return InterestResponse.builder()
                .id(data.getId())
                .value(data.getValue())
                .label(data.getLabel())
                .build();
    }
}

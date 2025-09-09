package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.response.ServiceResponse;
import com.example.VolunteerHub.entity.Services;
import org.mapstruct.Mapper;

@Mapper
public class ServiceMapper {
    public static ServiceResponse toServiceResponse(Services service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .title(service.getTitle())
                .durationInDays(service.getDurationInDays())
                .maxPosts(service.getMaxPosts())
                .build();
    }
}

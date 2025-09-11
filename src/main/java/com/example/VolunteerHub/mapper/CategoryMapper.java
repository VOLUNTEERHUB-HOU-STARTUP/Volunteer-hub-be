package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.request.CategoryCreationRequest;
import com.example.VolunteerHub.dto.response.CategoryResponse;
import com.example.VolunteerHub.entity.Categories;
import org.mapstruct.Mapper;

@Mapper
public class CategoryMapper {
    public static CategoryResponse toResponse(Categories category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .value(category.getValue())
                .label(category.getLabel())
                .build();
    }

    public static Categories toCategory(CategoryCreationRequest request) {
        return Categories.builder()
                .value(request.getValue())
                .label(request.getLabel())
                .build();
    }
}

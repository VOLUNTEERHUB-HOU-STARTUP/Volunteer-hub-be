package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.CategoryCreationRequest;
import com.example.VolunteerHub.dto.response.CategoryResponse;
import com.example.VolunteerHub.dto.response.InterestResponse;
import com.example.VolunteerHub.entity.Categories;
import com.example.VolunteerHub.entity.Interests;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.CategoryMapper;
import com.example.VolunteerHub.mapper.InterestMapper;
import com.example.VolunteerHub.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        List<Categories> categories = categoryRepository.findAll();

        return categories.stream().map(CategoryMapper::toResponse).toList();
    }

    public Categories getCategoryByString(String stringText) {
        Categories category = categoryRepository.findByValue(stringText.toLowerCase());

        if (category == null)
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);

        return category;
    }

    public CategoryResponse getDetail(String stringText) {
        Categories category = categoryRepository.findByValue(stringText.toLowerCase());

        if (category == null) throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);

        return CategoryMapper.toResponse(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createCategory(CategoryCreationRequest request) {
        if (categoryRepository.existsByValue(request.getValue()))
            throw new AppException(ErrorCode.CATEGORY_EXISTED);

        request.setValue(request.getValue().toLowerCase());

        Categories category = CategoryMapper.toCategory(request);

        categoryRepository.save(category);
    }
}

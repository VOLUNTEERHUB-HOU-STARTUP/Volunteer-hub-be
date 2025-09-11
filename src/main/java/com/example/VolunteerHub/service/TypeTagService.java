package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.TypeTagRequest;
import com.example.VolunteerHub.dto.response.TypeTagResponse;
import com.example.VolunteerHub.entity.TypeTags;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.TypeTagMapper;
import com.example.VolunteerHub.repository.TypeTagRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeTagService {
    TypeTagRepository typeTagRepository;

    public List<TypeTagResponse> getAll() {
        List<TypeTags> interests = typeTagRepository.findAll();

        return interests.stream().map(TypeTagMapper::toResponse).toList();
    }

    public TypeTags getTypeTagByString(String stringText) {
        TypeTags typeTag = typeTagRepository.findByValue(stringText.toLowerCase());

        if (typeTag == null)
            throw new AppException(ErrorCode.TYPE_TAG_NOT_FOUND);

        return typeTag;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createTypeTag(TypeTagRequest request) {
        if (typeTagRepository.existsByValue(request.getValue()))
            throw new AppException(ErrorCode.TYPE_TAG_EXISTED);

        TypeTags typeTag = TypeTagMapper.toTypeTag(request);

        typeTagRepository.save(typeTag);
    }
}

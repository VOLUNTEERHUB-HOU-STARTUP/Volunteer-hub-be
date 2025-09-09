package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.ServiceCreationRequest;
import com.example.VolunteerHub.dto.response.ServiceResponse;
import com.example.VolunteerHub.entity.Services;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.ServiceMapper;
import com.example.VolunteerHub.repository.ServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceService {
    ServiceRepository serviceRepository;

    public List<ServiceResponse> getListService(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Services> servicesList = serviceRepository.findAll(pageable);

        return servicesList.stream().map(ServiceMapper::toServiceResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void createService(ServiceCreationRequest request) {
        if (serviceRepository.existsByTitle(request.getTitle().toUpperCase()))
            throw new AppException(ErrorCode.SERVICE_EXISTED);

        Services service = Services.builder()
                .title(request.getTitle())
                .durationInDays(request.getDurationInDays())
                .maxPosts(request.getMaxPosts())
                .build();

        serviceRepository.save(service);
    }
}

package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.InterestRequest;
import com.example.VolunteerHub.dto.response.InterestResponse;
import com.example.VolunteerHub.entity.Interests;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.InterestMapper;
import com.example.VolunteerHub.repository.InterestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InterestService {
    InterestRepository interestRepository;

    public List<InterestResponse> getAll() {
        List<Interests> interests = interestRepository.findAll();

        return interests.stream().map(InterestMapper::toResponse).toList();
    }

    public Interests getInterestByString(String stringText) {
        Interests interest = interestRepository.findByValue(stringText.toLowerCase());

        if (interest == null)
            throw new AppException(ErrorCode.INTEREST_NOT_FOUND);

        return interest;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createInterest(InterestRequest request) {
        if (interestRepository.existsByValue(request.getValue()))
            throw new AppException(ErrorCode.INTEREST_EXISTED);

        request.setValue(request.getValue().toLowerCase());

        Interests interest = InterestMapper.toCInterests(request);

        interestRepository.save(interest);
    }
}

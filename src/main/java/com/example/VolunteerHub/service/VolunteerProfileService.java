package com.example.VolunteerHub.service;

import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.VolunteerProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VolunteerProfileService {
    VolunteerProfileRepository volunteerProfileRepository;

    @Transactional
    public void updateTotalEventJoined(UUID userId) {
        int updated = volunteerProfileRepository.incrementEventJoined(userId);

        if (updated == 0)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    @Transactional
    public void updateTotalEventRegistered(UUID userId) {
        int updated = volunteerProfileRepository.incrementEventRegistered(userId);

        if (updated == 0)
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }
}

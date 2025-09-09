package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.response.NotificationResponse;
import com.example.VolunteerHub.entity.Notifications;
import com.example.VolunteerHub.mapper.NotificationMapper;
import com.example.VolunteerHub.repository.NotificationRepository;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationService {
    NotificationRepository notificationRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> getNotificationResponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Notifications> listNotification = notificationRepository.findAll(pageable);

        return listNotification.map(NotificationMapper::toNotificationResponse).toList();
    }


}

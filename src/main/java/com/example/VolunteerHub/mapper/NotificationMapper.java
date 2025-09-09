package com.example.VolunteerHub.mapper;

import com.example.VolunteerHub.dto.response.NotificationResponse;
import com.example.VolunteerHub.entity.Notifications;
import org.mapstruct.Mapper;

@Mapper
public class NotificationMapper {
    public static NotificationResponse toNotificationResponse(Notifications notification) {
        return NotificationResponse.builder()
                .title(notification.getTitle())
                .body(notification.getBody())
                .time(notification.getTime())
                .read(notification.isRead())
                .link(notification.getLink())
                .build();
    }
}

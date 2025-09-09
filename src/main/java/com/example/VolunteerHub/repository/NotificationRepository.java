package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, UUID> {

}

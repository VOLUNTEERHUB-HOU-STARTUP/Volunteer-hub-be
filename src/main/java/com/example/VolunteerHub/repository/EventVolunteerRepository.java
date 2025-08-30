package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.EventVolunteers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventVolunteerRepository extends JpaRepository<EventVolunteers, UUID> {
    @Query("select case " +
            "when count(e) > 0 then true else false end from EventVolunteers e " +
            "where e.user.id = :userId and e.event.id = :eventId")
    boolean checkExisted(UUID userId, UUID eventId);

}

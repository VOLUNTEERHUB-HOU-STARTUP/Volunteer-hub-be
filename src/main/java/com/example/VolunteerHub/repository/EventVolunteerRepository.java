package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.EventVolunteers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventVolunteerRepository extends JpaRepository<EventVolunteers, UUID> {
    @Query("select case when count(ev) > 0 then true else false end " +
            "from EventVolunteers ev " +
            "where ev.user.id = :userId and ev.event.id = :eventId")
    boolean checkExisted(UUID userId, UUID eventId);

    @Query("select ev from EventVolunteers ev " +
            "where ev.event.id = :eventId")
    Page<EventVolunteers> findByEventId(UUID eventId, Pageable pageable);

    @Query("select ev from EventVolunteers ev " +
            "where ev.event.id = :eventId and ev.user.id = :userId")
    EventVolunteers findByEventIdAndVolunteerId(UUID eventId, UUID userId);
}

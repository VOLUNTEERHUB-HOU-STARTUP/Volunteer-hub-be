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

    @Query("select case when count(ev) > 0 then true else false end " +
            "from EventVolunteers ev " +
            "where ev.user.id = :userId and ev.event.slug = :slug")
    boolean checkExistedBySlug(UUID userId, String slug);

    @Query("select ev from EventVolunteers ev " +
            "where ev.event.id = :eventId")
    Page<EventVolunteers> findByEventId(UUID eventId, Pageable pageable);

    @Query("select ev from EventVolunteers ev " +
            "where ev.event.id = :eventId and ev.user.id = :userId")
    EventVolunteers findByEventIdAndVolunteerId(UUID eventId, UUID userId);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.event.user.id = :organizerId " +
            "ORDER BY ev.createdAt DESC")
    Page<EventVolunteers> findLatestVolunteersByOrganizer(UUID organizerId, Pageable pageable);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.user.id = :userId AND ev.status = 'PENDING' " +
            "ORDER BY ev.createdAt DESC")
    Page<EventVolunteers> findPendingEventsByUser(UUID userId, Pageable pageable);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.user.id = :userId AND ev.status = 'ACCEPTED' " +
            "ORDER BY ev.createdAt DESC")
    Page<EventVolunteers> findAcceptedEventsByUser(UUID userId, Pageable pageable);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.user.id = :userId AND ev.status = 'REJECTED' " +
            "ORDER BY ev.createdAt DESC")
    Page<EventVolunteers> findRejectedEventsByUser(UUID userId, Pageable pageable);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.event.slug = :slug AND ev.status = 'PENDING'")
    Page<EventVolunteers> findPendingVolunteersByEventId(String slug, Pageable pageable);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.event.slug = :slug AND ev.status = 'ACCEPTED'")
    Page<EventVolunteers> findAcceptedVolunteersByEventId(String slug, Pageable pageable);

    @Query("SELECT ev FROM EventVolunteers ev " +
            "WHERE ev.event.slug = :slug AND ev.status = 'REJECTED'")
    Page<EventVolunteers> findRejectedVolunteersByEventId(String slug, Pageable pageable);
}

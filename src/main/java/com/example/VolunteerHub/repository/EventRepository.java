package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Events, UUID> {
    @Query("select e from Events e")
    Page<Events> getEventWithPaging(Pageable pageable);

    @Query("select case " +
            "when count(e) > 0 then true else false end from Events e " +
            "where e.title = :title")
    boolean isExistedByEventTitle(String title);

    @Query("select exists (select 1 from Events e " +
            "where e.id = :eventId and e.user.id = :userId)")
    boolean isEventOwner(UUID userId, UUID eventId);

    @Query("SELECT e FROM Events e " +
            "WHERE e.isPublished = true AND e.endAt > :now")
    Page<Events> findPublishedActiveEvents(LocalDateTime now, Pageable pageable);
}

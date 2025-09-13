package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Events;
import com.example.VolunteerHub.enums.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Events, UUID> {

    @Query("SELECT e FROM Events e")
    Page<Events> getEventWithPaging(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Events e " +
            "WHERE e.title = :title")
    boolean isExistedByEventTitle(@Param("title") String title);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Events e " +
            "WHERE e.id = :eventId AND e.user.id = :userId")
    boolean isEventOwner(@Param("userId") UUID userId, @Param("eventId") UUID eventId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Events e " +
            "WHERE e.slug = :slug AND e.user.id = :userId")
    boolean isEventOwnerBySlug(@Param("userId") UUID userId, @Param("slug") String slug);

    @Query(" SELECT e FROM Events e " +
            "JOIN e.schedule s " +
            "WHERE e.status = :status AND s.endAt > :now")
    Page<Events> findPublishedActiveEvents(@Param("now") LocalDateTime now,
                                           @Param("status") EventStatusEnum status,
                                           Pageable pageable);

    @Query("""
        SELECT e FROM Events e 
        WHERE e.user.id = :userId 
        ORDER BY e.schedule.createdAt DESC
        """)
    Page<Events> findEventByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT e FROM Events e 
        WHERE e.user.id = :userId 
          AND e.status = :status 
        ORDER BY e.schedule.createdAt DESC
        """)
    Page<Events> findPublishedEventsByUserId(@Param("userId") UUID userId,
                                             @Param("status") EventStatusEnum status,
                                             Pageable pageable);

    @Query("""
        SELECT e FROM Events e 
        WHERE e.user.id = :userId 
          AND e.status <> :status 
        ORDER BY e.schedule.createdAt DESC
        """)
    Page<Events> findUnpublishedEventsByUserId(@Param("userId") UUID userId,
                                               @Param("status") EventStatusEnum status,
                                               Pageable pageable);

    @Query("SELECT e FROM Events e WHERE e.slug = :slug")
    Events findBySlug(@Param("slug") String slug);

    @Query("""
        SELECT e FROM Events e 
        JOIN e.schedule s
        WHERE e.status = :status 
          AND s.endAt < :now
        """)
    Page<Events> findExpiredEvents(@Param("now") LocalDateTime now,
                                   @Param("status") EventStatusEnum status,
                                   Pageable pageable);

    Page<Events> findByStatus(EventStatusEnum status, Pageable pageable);
}

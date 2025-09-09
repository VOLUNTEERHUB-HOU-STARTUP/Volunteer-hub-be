package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.VolunteerProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfiles, UUID> {
    VolunteerProfiles findByUserId(UUID userId);

    @Modifying
    @Query("UPDATE VolunteerProfiles vp " +
            "SET vp.totalEventJoined = vp.totalEventJoined + 1 " +
            "WHERE vp.user.id = :userId")
    int incrementEventJoined(UUID userId);

    @Modifying
    @Query("UPDATE VolunteerProfiles vp " +
            "SET vp.totalEventRegistered = vp.totalEventRegistered + 1 " +
            "WHERE vp.user.id = :userId")
    int incrementEventRegistered(UUID userId);
}

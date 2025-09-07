package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.OrganizerProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizerProfileRepository extends JpaRepository<OrganizerProfiles, UUID> {
    OrganizerProfiles findByUserId(UUID userId);
}

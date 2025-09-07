package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.VolunteerProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfiles, UUID> {
    VolunteerProfiles findByUserId(UUID userId);
}

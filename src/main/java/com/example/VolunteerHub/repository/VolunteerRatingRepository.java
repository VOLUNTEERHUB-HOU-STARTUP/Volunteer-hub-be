package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.VolunteerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VolunteerRatingRepository extends JpaRepository<VolunteerRating, UUID> {
    boolean existsByVolunteerIdAndOrganizerId(UUID volunteerId, UUID organizerId);

    @Query("SELECT AVG(vr.rating) FROM VolunteerRating vr " +
            "WHERE vr.volunteer.id = :volunteerId")
    float getAverageRating(UUID volunteerId);
}

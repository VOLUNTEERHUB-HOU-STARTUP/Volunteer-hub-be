package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profiles, UUID> {
}

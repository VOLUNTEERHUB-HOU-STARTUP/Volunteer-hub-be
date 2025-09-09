package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Services, UUID> {
    boolean existsByTitle(String title);
}

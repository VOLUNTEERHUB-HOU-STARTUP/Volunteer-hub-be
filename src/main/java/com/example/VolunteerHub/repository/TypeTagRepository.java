package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.TypeTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TypeTagRepository extends JpaRepository<TypeTags, UUID> {
    @Query("SELECT t FROM TypeTags t WHERE LOWER(t.value) = LOWER(:value)")
    TypeTags findByValue(String value);

    boolean existsByValue(String value);
}

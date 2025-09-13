package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Categories;
import com.example.VolunteerHub.entity.RequiredSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RequiredSkillRepository extends JpaRepository<RequiredSkills, UUID> {
    @Query("SELECT s FROM RequiredSkills s WHERE LOWER(s.value) = LOWER(:value)")
    RequiredSkills findByValue(String value);

    boolean existsByValue(String value);
}

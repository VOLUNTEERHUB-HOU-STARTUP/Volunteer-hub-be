package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Categories;
import com.example.VolunteerHub.entity.Interests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InterestRepository extends JpaRepository<Interests, UUID> {
    @Query("SELECT i FROM Interests i WHERE LOWER(i.value) = LOWER(:value)")
    Interests findByValue(String value);

    boolean existsByValue(String value);
}

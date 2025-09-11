package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, UUID> {
    @Query("select c from Categories c " +
            "where c.value = :value")
    Categories findByValue(String value);

    boolean existsByValue(String value);
}

package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidatedToken, String> {
}

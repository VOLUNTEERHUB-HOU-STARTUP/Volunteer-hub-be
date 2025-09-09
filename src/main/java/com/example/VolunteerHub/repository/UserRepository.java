package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);

    @Query("select u from Users u " +
            "where u.role.id = :roleId")
    Page<Users> findByRole(int roleId, Pageable pageable);
}

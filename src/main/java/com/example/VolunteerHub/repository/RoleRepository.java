package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.Roles;
import com.example.VolunteerHub.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findByRole(RoleEnum role);
}

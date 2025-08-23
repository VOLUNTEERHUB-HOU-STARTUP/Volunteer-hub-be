package com.example.VolunteerHub.repository;

import com.example.VolunteerHub.entity.UserRoles;
import com.example.VolunteerHub.entity.key.UserRolesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, UserRolesKey> {
}

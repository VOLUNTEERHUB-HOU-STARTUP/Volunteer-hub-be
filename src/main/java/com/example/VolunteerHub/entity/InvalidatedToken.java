package com.example.VolunteerHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidatedToken {
    @Id
    private String id;
    private Date expiryTime;
}

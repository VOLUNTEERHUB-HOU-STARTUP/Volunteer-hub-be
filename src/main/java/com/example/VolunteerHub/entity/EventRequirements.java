package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.ExperienceEnum;
import com.example.VolunteerHub.enums.SexEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "max_volunteer")
    int maxVolunteer;

    @Column(name = "min_age")
    int minAge;

    @Column(name = "max_age")
    int maxAge;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    SexEnum sex;

    @Column(name = "experience")
    @Enumerated(EnumType.STRING)
    ExperienceEnum experience;

    // event
    @OneToOne
    @JoinColumn(name = "event_id", nullable = false)
    Events event;
}

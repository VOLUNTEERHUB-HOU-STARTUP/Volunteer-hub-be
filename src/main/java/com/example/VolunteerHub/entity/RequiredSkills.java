package com.example.VolunteerHub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequiredSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "value", unique = true)
    String value;

    @Column(name = "label")
    String label;

    @OneToMany(mappedBy = "requiredSkill", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventRequiredSkill> eventRequiredSkills;
}

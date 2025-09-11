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
public class TypeTags {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "value")
    String value;

    @Column(name = "label")
    String label;

    // event type tag
    @OneToMany(mappedBy = "typeTag", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventTypeTag> eventTypeTags;
}

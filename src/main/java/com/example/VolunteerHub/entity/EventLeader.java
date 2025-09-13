package com.example.VolunteerHub.entity;

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
public class EventLeader {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "leader_name")
    String leaderName;

    @Column(name = "leader_phone")
    String leaderPhone;

    @Column(name = "leader_email")
    String leader_email;

    @Column(name = "sub_contact")
    String subContact; // liên hệ

    //event
    @OneToOne
    @JoinColumn(name = "event_id")
    Events event;
}

package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.AccountStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Table(
        indexes = {
                @Index(name = "idx_user_role", columnList = "role_id")
        }
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "status")
            @Enumerated(EnumType.STRING)
    AccountStatusEnum status;

    // profiles
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Profiles profile;

    // roles
    @ManyToOne
    @JoinColumn(name = "role_id")
    Roles role;

    // events
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Events> events = new ArrayList<>();

    // event volunteer
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventVolunteers> eventVolunteers = new ArrayList<>();

    //authProvider
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserAuthProvider> userAuthProviders = new ArrayList<>();

    // if volunteer
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    VolunteerProfiles volunteerProfile;

    // if organizer
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    OrganizerProfiles organizerProfile;

    // notification
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Notifications> listNotification;

    // Content
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Contents> listContent;

    // organizer service
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrganizerService> listOrganizerService;

    // rating
    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    List<VolunteerRating> listVolunteerRating;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    List<VolunteerRating> listOrganizerRating;

}

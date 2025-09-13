package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.*;
import com.example.VolunteerHub.utils.SlugUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Table(
        indexes = {
                @Index(name = "idx_event_slug", columnList = "slug")
        }
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "slug", unique = true, nullable = false)
    String slug;

    @Column(name = "salary")
    float salary;

    @Column(name = "location", nullable = false)
    String location;

    @Column(name = "detail_location")
    String detailLocation;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    EventStatusEnum status;

    @Column(name = "auto_accept")
    boolean autoAccept;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    EventTypeEnum type;

    @Column(name = "cover_image")
    String coverImage;

    @Column(name = "online")
    boolean online;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    PriorityEnum priority;

    // event leader
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    EventLeader leader;

    // event schedule
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    EventSchedule schedule;

    // event requirements
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    EventRequirements requirements;

    // event type tag
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventTypeTag> eventTypeTags;

    // event interest
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventInterest> eventInterests;

    // event category
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventCategory> eventCategories;

    // required skill
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventRequiredSkill> eventRequiredSkills;

    // event like
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventLikes> eventLikes;

    // event comment
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventComments> eventComments;

    // media
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventMedias> eventMedia;

    //auto generate title -> slug
    @PrePersist
    @PreUpdate
    public void generateSlung() {
        if (this.title != null && (this.slug == null || this.slug.isEmpty())) {
            this.slug = SlugUtil.toSlug(this.title);
        }
    }

    // event volunteer
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EventVolunteers> eventVolunteers;

    // notification
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Notifications> listNotification;
}

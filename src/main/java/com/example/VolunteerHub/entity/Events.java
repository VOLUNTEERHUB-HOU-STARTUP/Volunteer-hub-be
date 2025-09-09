package com.example.VolunteerHub.entity;

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
                @Index(name = "idx_event_isPublished", columnList = "is_published"),
                @Index(name = "idx_event_slug", columnList = "slug"),
                @Index(name = "idx_event_startAt", columnList = "start_at"),
                @Index(name = "idx_event_deadline", columnList = "deadline")
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

    @Column(name = "start_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startAt;

    @Column(name = "end_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endAt;

    @Column(name = "max_volunteer")
    int maxVolunteer;

    @Column(name = "is_published")
    boolean isPublished;

    @Column(name = "auto_accept")
    boolean autoAccept;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt;

    @Column(name = "deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime deadline;

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

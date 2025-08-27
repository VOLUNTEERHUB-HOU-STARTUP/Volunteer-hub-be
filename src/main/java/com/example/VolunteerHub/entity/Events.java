package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.Utils.SlugUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startAt;

    @Column(name = "end_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endAt;

    @Column(name = "max_volunteer")
    int maxVolunteer;

    @Column(name = "is_published")
    boolean isPublished;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt;

    @Column(name = "deadline")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
}

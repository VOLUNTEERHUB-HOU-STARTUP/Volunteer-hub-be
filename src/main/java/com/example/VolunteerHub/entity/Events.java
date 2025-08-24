package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.Utils.SlugUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
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

    @Column(name = "start_at")
    Instant startAt;

    @Column(name = "end_at")
    Instant endAt;

    @Column(name = "max_volunteer")
    int maxVolunteer;

    @Column(name = "is_published")
    boolean isPublished;

    @Column(name = "created_at")
    Instant createdAt = Instant.now();

    //auto generate title -> slug
    public void generateSlung() {
        if (this.title != null && (this.slug == null || this.slug.isEmpty())) {
            this.slug = SlugUtil.toSlug(this.title);
        }
    }
}

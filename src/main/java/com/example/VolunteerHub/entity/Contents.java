package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.ContentStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String title; // tiêu đề bài đăng/cuộc thi

    @Column(columnDefinition = "TEXT")
    String description;

    String thumbnailUrl; // ảnh đại diện/bìa

    @Enumerated(EnumType.STRING)
    ContentStatusEnum status;

    LocalDateTime startDate;
    LocalDateTime endDate;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    Users createdBy;
}

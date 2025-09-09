package com.example.VolunteerHub.entity;

import com.example.VolunteerHub.enums.ReportTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Table(
        indexes = {
                @Index(name = "idx_report_resolved", columnList = "resolved")
        }
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.STRING)
    ReportTypeEnum type;
    String content;
    boolean resolved;

    // notification
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Notifications> listNotification;
}

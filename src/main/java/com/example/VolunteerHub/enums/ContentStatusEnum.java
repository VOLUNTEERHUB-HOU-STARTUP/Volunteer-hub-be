package com.example.VolunteerHub.enums;

public enum ContentStatusEnum {
    DRAFT,      // bản nháp
    PENDING,    // chờ duyệt (Organizer đăng thì vào đây)
    APPROVED,   // đã duyệt
    REJECTED,   // bị từ chối
    PUBLISHED,  // đã xuất bản
    EXPIRED     // hết hạn (nếu có startDate/endDate)
}

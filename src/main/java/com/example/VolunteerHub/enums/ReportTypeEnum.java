package com.example.VolunteerHub.enums;

import lombok.Getter;

@Getter
public enum ReportTypeEnum {
    ABUSE,                 // Lạm dụng
    SPAM,                  // Spam
    FAKE_INFORMATION,      // Thông tin giả mạo
    INAPPROPRIATE_CONTENT, // Nội dung không phù hợp
    SCAM,                  // Lừa đảo
    HATE_SPEECH,           // Ngôn từ thù ghét
    COPYRIGHT,             // Vi phạm bản quyền
    OTHER                  // Khác
}

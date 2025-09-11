package com.example.VolunteerHub.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // 1000 - 1099: User
    USER_EXISTED(1000, "user existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1001, "user not existed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1002, "username is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "password is invalid", HttpStatus.BAD_REQUEST),
    NAME_SHORT(1004, "name is too short", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1005, "wrong password", HttpStatus.BAD_REQUEST),

    // 1100 - 1199: Profile
    PROFILE_NOT_EXISTED(1100, "profile not existed", HttpStatus.NOT_FOUND),
    NAME_CHANGE_TOO_SOON(1101, "name change too soon", HttpStatus.BAD_REQUEST),
    NAME_INVALID(1102, "name is limited to 3-20 letters only", HttpStatus.BAD_REQUEST),

    // 2000 - 2099: Event
    EVENT_NOT_EXISTED(2000, "event not existed", HttpStatus.NOT_FOUND),
    EVENT_EXISTED(2001, "event existed", HttpStatus.BAD_REQUEST),
    EVENT_NOT_PUBLISH(2002, "event do not public for everyone", HttpStatus.BAD_REQUEST),

    // 2100 - 2199: Category
    CATEGORY_NOT_FOUND(2100, "category not found", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(2101, "category existed", HttpStatus.BAD_REQUEST),

    // 2200 - 2299: Required skill
    REQUIRED_SKILL_NOT_FOUND(2200, "required skill not found", HttpStatus.NOT_FOUND),
    REQUIRED_SKILL_EXISTED(2201, "required skill existed", HttpStatus.BAD_REQUEST),

    // 2300 - 2399: type tag
    TYPE_TAG_NOT_FOUND(2300, "type tag not found", HttpStatus.NOT_FOUND),
    TYPE_TAG_EXISTED(2301, "type tag existed", HttpStatus.BAD_REQUEST),

    // 2400 - 2499: type tag
    INTEREST_NOT_FOUND(2400, "interest not found", HttpStatus.NOT_FOUND),
    INTEREST_EXISTED(2401, "interest existed", HttpStatus.BAD_REQUEST),

    // 3000 - 3099: Event volunteers
    EVENT_VOLUNTEER_NOT_EXISTED(3000, "event volunteer not existed", HttpStatus.NOT_FOUND),

    // 3100 - 3199: Volunteer rating
    RATING_EXISTED(3100, "rating existed", HttpStatus.BAD_REQUEST),

    // 4000 - 4099: ROLE
    ROLE_NOT_FOUND(4000, "role not found", HttpStatus.NOT_FOUND),

    // 4100 - 4199: Service
    SERVICE_EXISTED(4100, "service existed", HttpStatus.BAD_REQUEST),

    // 5000 - 5099: Notification
    NOTIFICATION_NOT_EXISTED(5000, "notification not existed", HttpStatus.NOT_FOUND),
    NOTIFICATION_FORBIDEN(5001, "you don't have permission for this notification", HttpStatus.BAD_REQUEST),

    // 6000 - 6099: Message
    MESSAGE_NOT_EXISTED(6000, "message not existed", HttpStatus.NOT_FOUND),

    // 7000 - 7099: Validation
    VALIDATION_ERROR(7000, "validation error", HttpStatus.BAD_REQUEST),

    // 8000 - 8099: File & Media
    FILE_UPLOAD_ERROR(8000, "file upload error", HttpStatus.BAD_REQUEST),
    INVALID_MEDIA_TYPE(8001, "invalid media type", HttpStatus.BAD_REQUEST),

    // 9000 - 9099: Auth & General
    UNAUTHORIZED(9000, "not have permission", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(9001, "token is not valid", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(9002, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_RESOURCE(9003, "duplicate resource", HttpStatus.BAD_REQUEST),
    INVALID_KEY(9004, "invalid message key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

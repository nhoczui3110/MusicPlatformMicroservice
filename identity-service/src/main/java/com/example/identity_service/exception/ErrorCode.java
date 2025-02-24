package com.example.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(9998, "Invalid key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1000, "User existed!", HttpStatus.BAD_REQUEST),
    PASSWORD_MIN(1001, "Password must be at least {min} character", HttpStatus.BAD_REQUEST),
    USERNAME_MIN(1002, "Username must be at least {min} character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1003, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You dont have permission", HttpStatus.FORBIDDEN),
    DOB_INVALID(1006, "Day of birth must be at least {min}", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(1007, "Email is required!", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1008, "Password is wrong!", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_NOT_MATCH(1009, "New password not match!", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1010, "Email is invalid", HttpStatus.BAD_REQUEST),
    USER_ID_REQUIRED(1011, "User Id is required!", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_REQUIRED(1012, "Old password is required!", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_REQUIRED(1013, "New password is required!", HttpStatus.BAD_REQUEST),
    CONFIRM_NEW_PASSWORD_REQUIRED(1014, "Confirm new password is required!", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private  HttpStatusCode statusCode;

}

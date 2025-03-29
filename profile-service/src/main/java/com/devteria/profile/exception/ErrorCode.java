package com.devteria.profile.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(9998, "Invalid key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You dont have permission", HttpStatus.FORBIDDEN),
    PROFILE_NOT_FOUND(1006, "Profile not found", HttpStatus.NOT_FOUND),
    NOT_FOLLOWING(1007, "You don't follow this user!", HttpStatus.BAD_REQUEST),
    ALREADY_FOLLOWING(1008, "Already following!", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1014, "Email is existed", HttpStatus.BAD_REQUEST)
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

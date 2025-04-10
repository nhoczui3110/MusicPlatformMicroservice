package com.MusicPlatForm.file_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    INVALID_KEY(9998, "Invalid key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You dont have permission", HttpStatus.FORBIDDEN),
    TRACK_FILE_NOT_FOUND(1006, "Track file not found", HttpStatus.FORBIDDEN),
    AVATAR_FILE_NOT_FOUND(1007, "Avartar image file not found", HttpStatus.FORBIDDEN),
    COVER_FILE_NOT_FOUND(1008, "Cover image file not found", HttpStatus.FORBIDDEN),
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

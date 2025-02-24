package com.example.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.bridge.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank(message = "USER_ID_REQUIRED")
    String userId;
    @NotBlank(message = "OLD_PASSWORD_REQUIRED")
    String oldPassword;
    @NotBlank(message = "NEW_PASSWORD_REQUIRED")
    String newPassword;
    @NotBlank(message = "CONFIRM_NEW_PASSWORD_REQUIRED")
    String confirmNewPassword;
}

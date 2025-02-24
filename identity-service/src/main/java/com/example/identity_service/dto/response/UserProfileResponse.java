package com.example.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserProfileResponse {
    String id;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
    String userId;
}

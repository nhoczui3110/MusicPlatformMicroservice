package com.devteria.profile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileWithCountFollowResponse {
    String id;
    String firstName;
    String lastName;
    String displayName;
    LocalDate dob;
    Boolean gender;
    String email;
    String cover;
    String avatar;
    String userId;
    int followerCount;
    int followingCount;
    boolean isFollowing;
    public boolean isFollowing(){
        return this.isFollowing;
    }
}

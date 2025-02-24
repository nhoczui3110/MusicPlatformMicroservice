package com.devteria.profile.mapper;

import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.request.ProfileUpdateRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
    void updateUserProfile(@MappingTarget UserProfile userProfile, ProfileUpdateRequest request);
}

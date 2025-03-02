package com.MusicPlatForm.search_service.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.MusicPlatForm.search_service.Dto.Response.UserResponse;
import com.MusicPlatForm.search_service.Entity.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    @Mapping(target = "type", constant = "user") 
    UserResponse toUserResponse(User user);
    @Mapping(target = "type", constant = "user") 

    List<UserResponse> toUserResponseList(List<User> users);
}

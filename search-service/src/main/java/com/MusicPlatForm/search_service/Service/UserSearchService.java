package com.MusicPlatForm.search_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.search_service.Dto.Response.UserResponse;
import com.MusicPlatForm.search_service.Entity.User;
import com.MusicPlatForm.search_service.Mapper.UserResponseMapper;
import com.MusicPlatForm.search_service.Repository.UserRepository;

@Service
public class UserSearchService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserResponseMapper userResponseMapper;

    public UserSearchService(UserRepository userRepository){
        this.userRepository = userRepository;
    }   
    public List<UserResponse> searchUsers(String query){
        List<User> users  =  this.userRepository.findUsers(query);
        return userResponseMapper.toUserResponseList(users);
    }
    public  List<UserResponse>  searchUsersByName(String query){
        List<User> users =  this.userRepository.findUserByName(query);
        return userResponseMapper.toUserResponseList(users);
    }
    public User save(User user){
        User savedUser = this.userRepository.save(user);
        return savedUser;
    }
}

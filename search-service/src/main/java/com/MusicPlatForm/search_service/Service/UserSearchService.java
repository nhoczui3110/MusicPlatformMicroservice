package com.MusicPlatForm.search_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MusicPlatForm.search_service.Dto.Request.UserRequest;
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
    public List<String> searchUsers(String query){
        List<User> users  =  this.userRepository.findUsers(query);
        return userResponseMapper.toUserIds(users);
    }
    // public  List<UserResponse>  searchUsersByName(String query){
    //     List<User> users =  this.userRepository.findUserByName(query);
    //     return userResponseMapper.toUserResponseList(users);
    // }
    public User save(UserRequest userRequest){
        User user = this.userResponseMapper.toUserEntity(userRequest);
        User savedUser = this.userRepository.save(user);
        return savedUser;
    }

    public void deleteUserByUserId(String userId) {
        userRepository.deleteByUserId(userId);
    }
}

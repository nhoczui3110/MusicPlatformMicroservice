package com.MusicPlatForm.search_service.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.MusicPlatForm.search_service.Dto.ApiResponse;
import com.MusicPlatForm.search_service.Dto.Response.UserResponse;
import com.MusicPlatForm.search_service.Entity.User;
import com.MusicPlatForm.search_service.Service.UserSearchService;

@RestController
@RequestMapping("/users")
public class SearchUserRestController {
    @Autowired
    private UserSearchService userSearchService;
    
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<User>> addUser(@RequestBody User user){
        User savedUser = userSearchService.save(user);
        return ResponseEntity.ok().body(
            ApiResponse.<User>builder().code(200).data(savedUser).message("Add user successfully").build()
        );
    }
    @GetMapping("")
    public ResponseEntity<?> searchUsers(@RequestParam String query){
        // return ResponseEntity.ok().body(
        //     this.userSearchService.searchUsers(query)
        // );
        return ResponseEntity.ok().body(
            ApiResponse.<List<UserResponse>>builder().code(200).data(this.userSearchService.searchUsers(query)).message("Successfully").build()
        );
    }
}

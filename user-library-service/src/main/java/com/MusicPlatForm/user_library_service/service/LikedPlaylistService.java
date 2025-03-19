package com.MusicPlatForm.user_library_service.service;

import org.springframework.stereotype.Service;

import com.MusicPlatForm.user_library_service.repository.LikedPlaylistRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikedPlaylistService {
    private LikedPlaylistRepository likedPlaylistRepository;
    
}

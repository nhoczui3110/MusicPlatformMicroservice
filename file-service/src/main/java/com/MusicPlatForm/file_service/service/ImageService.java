package com.MusicPlatForm.file_service.service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.MusicPlatForm.file_service.exception.AppException;

@Service
public interface ImageService {

    @PreAuthorize("isAuthenticated()")
    public String addAvatar(MultipartFile avatar) throws IOException;

    @PreAuthorize("isAuthenticated()")
    public String addcoverImage(MultipartFile coverImage) throws IOException;

    @PreAuthorize("isAuthenticated()")
    public void deleteAvatar(String avatarName) throws AppException, IOException ;

    public void deleteCover(String coverName) throws AppException, IOException ;

    @PreAuthorize("isAuthenticated()")
    public String replaceAvatar(MultipartFile avatar, String oldAvatarName) throws IOException, NoSuchFileException;
    
    @PreAuthorize("isAuthenticated()")
    public String replaceCover(MultipartFile cover, String oldCoverName) throws IOException, NoSuchFileException;
}


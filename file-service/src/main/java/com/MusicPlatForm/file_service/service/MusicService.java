package com.MusicPlatForm.file_service.service;

import java.io.IOException;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.response.AudioResponse;
import com.MusicPlatForm.file_service.exception.AppException;

@Service
public interface MusicService {

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public AudioResponse addAudio(MultipartFile audioFile) throws Exception,IOException;
    
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public List<AudioResponse> addAudios(List<MultipartFile> trackFiles) throws IOException;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void deleteAudio(String trackName) throws IOException, AppException;
}

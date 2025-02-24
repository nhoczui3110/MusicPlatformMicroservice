package com.MusicPlatForm.file_service.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @Value("file.upload-dir")
    private String uploadDir;

    public String addAvatar(MultipartFile image) throws IOException{
        String name = Instant.now().getEpochSecond() + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve("avatars").resolve(name);
        Files.write(filePath, image.getBytes());
        return name;
    }
    
}

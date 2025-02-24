package com.MusicPlatForm.file_service.rescontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.ApiResponse;
import com.MusicPlatForm.file_service.service.ImageService;

@RestController
@RequestMapping("/api/v1/image")
public class ImageRestController {
    private ImageService imageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    public ImageRestController(ImageService imageService){
        this.imageService =imageService;
    }

    @GetMapping("avatar/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException{

        Path path = Paths.get(uploadDir).resolve("avatars").resolve(filename);
        byte[] imageBytes = Files.readAllBytes(path);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Thay đổi loại MIME nếu cần
                .contentLength(imageBytes.length)
                .body(resource);
            
    }

    @PostMapping("add-avatar")
    public ResponseEntity<ApiResponse<String>> addAvatar(@RequestParam MultipartFile avatar) throws IOException{
        String avatarName = imageService.addAvatar(avatar);
        return ResponseEntity.ok().body(
                ApiResponse.<String>
                    builder()
                        .code(200)
                        .message("add successfully")
                        .data(avatarName).build()
                );
    }

}

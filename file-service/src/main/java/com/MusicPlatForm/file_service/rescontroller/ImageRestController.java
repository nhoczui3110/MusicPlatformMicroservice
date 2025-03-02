package com.MusicPlatForm.file_service.rescontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.ApiResponse;
import com.MusicPlatForm.file_service.dto.request.AvatarRequest;
import com.MusicPlatForm.file_service.dto.request.CoverRequest;
import com.MusicPlatForm.file_service.dto.response.AvatarResponse;
import com.MusicPlatForm.file_service.dto.response.CoverResponse;
import com.MusicPlatForm.file_service.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageRestController {
    private ImageService imageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    public ImageRestController(ImageService imageService){
        this.imageService =imageService;
    }

    /*
     * GET
     */
    @GetMapping("avatar/{filename}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) throws IOException{

        Path path = Paths.get(uploadDir).resolve("avatars").resolve(filename);
        byte[] imageBytes = Files.readAllBytes(path);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(imageBytes.length)
                .body(resource);
            
    }
    @GetMapping("cover/{filename}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable String filename) throws IOException{

        Path path = Paths.get(uploadDir).resolve("covers").resolve(filename);
        byte[] imageBytes = Files.readAllBytes(path);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(imageBytes.length)
                .body(resource);
            
    }

    /*
     * ADD
     */
    @PostMapping("add-avatar")
    public ResponseEntity<ApiResponse<AvatarResponse>> addAvatar(@RequestParam MultipartFile avatar) throws IOException{
        String avatarName = imageService.addAvatar(avatar);
        return ResponseEntity.ok().body(
                ApiResponse.<AvatarResponse>
                    builder()
                        .code(200)
                        .message("Added avatar successfully")
                        .data(
                            AvatarResponse.builder().avatarName(avatarName).build()
                        ).build()
                );
    }
    @PostMapping("add-cover")
    public ResponseEntity<ApiResponse<CoverResponse>> addCoverImage(@RequestParam MultipartFile cover) throws IOException{
        String coverName = imageService.addcoverImage(cover);
        return ResponseEntity.ok().body(
                ApiResponse.<CoverResponse>
                    builder()
                        .code(200)
                        .message("Added cover successfully")
                        .data(
                            CoverResponse.builder().coverName(coverName).build()
                        ).build()
                );
    }

    
    /*
     * DELETE
     */
    @DeleteMapping("delete-avatar")
    public ResponseEntity<ApiResponse<String>> deleteAvatar(@RequestBody AvatarRequest avatarRequest) throws IOException, NoSuchFileException{
        imageService.deleteAvatar(avatarRequest.getAvatarName());
        return ResponseEntity.ok().body(
            ApiResponse.<String>
                builder()
                    .code(200)
                    .message("Deleted avatar successfully")
                    .build()
            );
    }
    @DeleteMapping("delete-cover")
    public ResponseEntity<ApiResponse<String>> deleteCover(@RequestBody CoverRequest coverRequest) throws IOException, NoSuchFileException{
        imageService.deleteAvatar(coverRequest.getCoverName());
        return ResponseEntity.ok().body(
            ApiResponse.<String>
                builder()
                    .code(200)
                    .message("Deleted cover successfully")
                    .build()
            );
    }

    
    /*
     * PUT
     */

    @PutMapping("replace-avatar")
    public ResponseEntity<ApiResponse<AvatarResponse>> replaceAvatar(@RequestParam MultipartFile newAvatar,@RequestParam String oldAvatarName) throws IOException, NoSuchFileException{
        String newAvatarName = imageService.replaceAvatar(newAvatar, oldAvatarName);
        return ResponseEntity.ok().body(
                ApiResponse.<AvatarResponse>
                    builder()
                        .code(200)
                        .message("Replaced avatar successfully")
                        .data(
                            AvatarResponse.
                            builder().
                            avatarName(newAvatarName)
                            .build()
                            )
                        .build()
                );
    }
    @PutMapping("replace-cover")
    public ResponseEntity<ApiResponse<CoverResponse>> replaceCover(@RequestParam MultipartFile newCover,@RequestParam String oldCoverName) throws IOException, NoSuchFileException{
        String newCoverName = imageService.replaceCover(newCover, oldCoverName);
        return ResponseEntity.ok().body(
                ApiResponse.<CoverResponse>
                    builder()
                        .code(200)
                        .message("Replaced cover successfully")
                        .data(
                            CoverResponse.
                            builder().
                            coverName(newCoverName)
                            .build()
                            )
                        .build()
                );
    }
}

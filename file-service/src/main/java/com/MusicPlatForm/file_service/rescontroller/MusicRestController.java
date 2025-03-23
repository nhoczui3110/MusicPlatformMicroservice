package com.MusicPlatForm.file_service.rescontroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.ApiResponse;
import com.MusicPlatForm.file_service.dto.response.TrackResponse;
import com.MusicPlatForm.file_service.service.MusicService;

@RestController
@RequestMapping("/music")
public class MusicRestController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private MusicService musicService;
    public MusicRestController(MusicService musicService){
        this.musicService = musicService;
    }
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getTrack(@PathVariable String filename) throws IOException{
        Path path = Paths.get(uploadDir).resolve("musics").resolve(filename);

        Resource resource = new UrlResource(path.toUri());
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "audio/mpeg"; // default mp3
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping("/add-track")
    public ResponseEntity<ApiResponse<TrackResponse>> addTrack(@RequestPart MultipartFile track) throws Exception,IOException{
        TrackResponse trackResponse = musicService.addTrack(track);
        return ResponseEntity.ok()
                            .body(
                                ApiResponse
                                    .<TrackResponse>builder()
                                    .code(200)
                                    .message("Add track successfully")
                                    .data(trackResponse)
                                    .build()
                                );
    }
    @DeleteMapping("delete-track/{trackName}")
    public ResponseEntity<ApiResponse<String>> deleteTrack(@PathVariable String trackName) throws IOException,NoSuchFileException{
        musicService.deleteTrack(trackName);
        return ResponseEntity.ok()
        .body(
            ApiResponse
                .<String>builder()
                .code(200)
                .message("Delete track successfully")
                // .data("Deleted Successfully")
                .build()
            );
    }
}

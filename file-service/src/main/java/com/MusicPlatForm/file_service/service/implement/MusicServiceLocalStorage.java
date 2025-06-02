package com.MusicPlatForm.file_service.service.implement;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.dto.response.AudioResponse;
import com.MusicPlatForm.file_service.entity.Audio;
import com.MusicPlatForm.file_service.exception.AppException;
import com.MusicPlatForm.file_service.exception.ErrorCode;
import com.MusicPlatForm.file_service.repository.AudioRepository;
import com.MusicPlatForm.file_service.service.MusicService;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import java.lang.Math;

@Service
public class MusicServiceLocalStorage implements MusicService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Value("${file.musics-dir}")
    private String musicDir;

   private final AudioRepository audioRepository; // Inject AudioRepository

    public MusicServiceLocalStorage(AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }

    private String getDuration(String filePath) {
          long totalMilliseconds = 0;
          try (FileInputStream fis = new FileInputStream(filePath)) {
              Bitstream bitstream = new Bitstream(fis);
              Header header;
  
              while ((header = bitstream.readFrame()) != null) {
                  totalMilliseconds += header.ms_per_frame();
                  bitstream.closeFrame();
              }
  
              long totalSeconds = (int)Math.ceil(totalMilliseconds/1000.0);
              
              return (totalSeconds/60)+":"+totalSeconds%60;
          } catch (Exception e) {
              e.printStackTrace();
              return "";
          }
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public AudioResponse addAudio(MultipartFile audioFile) throws Exception,IOException{
        // Generate a unique file name
        String name = Instant.now().getEpochSecond() + audioFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(musicDir).resolve(name);

        // Save the file
        Files.write(filePath, audioFile.getBytes());

        // Get the duration of the track
        String duration = getDuration(filePath.toString());

        // Save the track to the database
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // Get user ID from Authentication

        Audio audioEntity = new Audio(name, duration, userId);
        audioRepository.save(audioEntity); // Save to the database

        // Return response
        return AudioResponse.builder()
                .duration(duration)
                .track(name)
                .build();
    }

    // Method to add multiple audio files and save them to the database
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public List<AudioResponse> addAudios(List<MultipartFile> trackFiles) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // Get user ID from Authentication

        List<AudioResponse> trackResponses = new ArrayList<>();
        for (MultipartFile trackFile : trackFiles) {
            // Generate a unique file name
            String name = Instant.now().getEpochSecond() + trackFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir).resolve(musicDir).resolve(name);

            // Save the file
            Files.write(filePath, trackFile.getBytes());

            // Get the duration of the track
            String duration = getDuration(filePath.toString());

            // Save to the database
            Audio audioEntity = new Audio(name, duration, userId);
            audioRepository.save(audioEntity);

            // Add to response list
            trackResponses.add(AudioResponse.builder()
                    .duration(duration)
                    .track(name)
                    .build());
        }
        return trackResponses;
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void deleteAudio(String trackName) throws IOException, AppException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName(); // Get user ID from Authentication
        Audio audio = this.audioRepository.findByFileName(trackName);
        if(!audio.getUserId().equals(userId))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Path filePath = Paths.get(uploadDir).resolve(musicDir).resolve(trackName);

        // Delete the file from the file system
        boolean isDeleted = Files.deleteIfExists(filePath);
        if (!isDeleted) throw new AppException(ErrorCode.TRACK_FILE_NOT_FOUND);

        // Delete the audio entity from the database
        audioRepository.delete(audio);;
    }
}

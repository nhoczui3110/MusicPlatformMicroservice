package com.MusicPlatForm.file_service.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.MusicPlatForm.file_service.entity.Avatar;
import com.MusicPlatForm.file_service.entity.Cover;
import com.MusicPlatForm.file_service.exception.AppException;
import com.MusicPlatForm.file_service.exception.ErrorCode;
import com.MusicPlatForm.file_service.repository.AvatarRepository;
import com.MusicPlatForm.file_service.repository.CoverRepository;

@Service
public class ImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Value("${file.avatars-dir}")
    private String avatarDir;
    @Value("${file.covers-dir}")
    private String coversDir;

    private AvatarRepository avatarRepository;
    private CoverRepository coverRepository;
    public ImageService( AvatarRepository avatarRepository,CoverRepository coverRepository){
        this.avatarRepository = avatarRepository;
        this.coverRepository = coverRepository;
    }

    private String addImage(MultipartFile image,String folder)throws IOException{
        String name = Instant.now().getEpochSecond() + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(folder).resolve(name);
        Files.write(filePath, image.getBytes());
        return name;
    }
    // @PreAuthorize("isAuthenticated()")
    public String addAvatar(MultipartFile avatar) throws IOException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        String avatarName = addImage(avatar, avatarDir);
        Avatar avatarEntity = new Avatar();
        avatarEntity.setFileName(avatarName);
        avatarEntity.setUserId(userId);
        this.avatarRepository.save(avatarEntity);
        return avatarName;
    }
    // @PreAuthorize("isAuthenticated()")
    public String addcoverImage(MultipartFile coverImage) throws IOException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        String coverName = addImage(coverImage, coversDir);
        Cover coverEntity = new Cover(coverName, userId);
        coverRepository.save(coverEntity);
       return coverName;
    }
    

    public Resource getImageResource(String filename,String folder) throws IOException{
        Path path = Paths.get(uploadDir).resolve(folder).resolve(filename);
        byte[] imageBytes = Files.readAllBytes(path);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return resource;
    }
    public Resource getCoverImage(String filename) throws IOException{
        return getImageResource(filename, coversDir);
    }
    public Resource getAvatarImage(String filename) throws IOException{
        return getImageResource(filename, avatarDir);
    }


    /*
     * Delete Image
     * @Param: 
     *  + name: image name
     *  + folder: foler is image stored
     */
    // @PreAuthorize("isAuthenticated()")
    public boolean deleteImage(String name,String folder)throws IOException{
        Path filePath = Paths.get(uploadDir).resolve(folder).resolve(name);

        boolean isDeleted = Files.deleteIfExists(filePath);
        return isDeleted;
    }

    // @PreAuthorize("isAuthenticated()")
    public void deleteAvatar(String avatarName) throws AppException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        Avatar avatar = this.avatarRepository.findById(avatarName).orElseThrow(()->new AppException(ErrorCode.AVATAR_FILE_NOT_FOUND));
        if(!avatar.getUserId().equals(userId)) throw new AppException(ErrorCode.UNAUTHORIZED);
        boolean isDeleted = deleteImage(avatarName, avatarDir);
        if(!isDeleted) throw new AppException(ErrorCode.AVATAR_FILE_NOT_FOUND);
    }
    public void deleteCover(String coverName) throws AppException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        Cover cover = this.coverRepository.findById(coverName).orElseThrow(()->new AppException(ErrorCode.COVER_FILE_NOT_FOUND));
        if(!cover.getUserId().equals(userId)) throw new AppException(ErrorCode.UNAUTHORIZED);
        boolean isDeleted = deleteImage(coverName, coversDir);
        if(!isDeleted) throw new AppException(ErrorCode.COVER_FILE_NOT_FOUND);
    }



    /*
     * Replace Image
     *
     */
    // @PreAuthorize("isAuthenticated()")
    public String repaceImage(MultipartFile image, String name, String folder) throws IOException, NoSuchFileException{
        boolean isDeleted = deleteImage(name, folder);
        if(!isDeleted) throw new NoSuchFileException("Image not found");
        String newName = addImage(image, folder);
        return newName;
    }

    // @PreAuthorize("isAuthenticated()")
    public String replaceAvatar(MultipartFile avatar, String oldAvatarName) throws IOException, NoSuchFileException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        Avatar avatarEntity = this.avatarRepository.findById(oldAvatarName).orElseThrow(()-> new AppException(ErrorCode.AVATAR_FILE_NOT_FOUND));
        if(!avatarEntity.getUserId().equals(userId)) throw new AppException(ErrorCode.UNAUTHORIZED);
        
        String avatarName = repaceImage(avatar, oldAvatarName, avatarDir);
        
        avatarEntity.setFileName(avatarName);
        return avatarName;
    }
    // @PreAuthorize("isAuthenticated()")
    public String replaceCover(MultipartFile cover, String oldCoverName) throws IOException, NoSuchFileException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
    
        Cover coverEntity = this.coverRepository.findById(oldCoverName)
            .orElseThrow(() -> new AppException(ErrorCode.COVER_FILE_NOT_FOUND));
    
        if (!coverEntity.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    
        String newCoverName = repaceImage(cover, oldCoverName, coversDir);
    
        coverEntity.setFileName(newCoverName);
        return newCoverName;
    }
}

package com.MusicPlatForm.user_library_service.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Playlist")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Playlist {
    @Id
    @Column(name = "Id")
    String id;

    @Column(name = "Title",nullable = false)
    String title;

    @Column(name = "Release_date")
    LocalDateTime releaseDate;
    
    @Column(name = "Description")
    String description;
    
    @Column(name = "Privacy",nullable = false)
    String privacy;

    @Column(name = "User_id",nullable = false)
    String userId;

    @Column(name = "Gener_id")
    String generId;

    @Column(name = "Image_path")
    String imagePath;

    // orphanRemoval = true -> delete automatically when remove playlist track out of list
    @OneToMany(mappedBy = "playlist",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    List<LikedPlaylist> likedPlaylists;
    
    @OneToMany(mappedBy = "playlist",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    List<PlaylistTrack> playlistTracks;

    

    @PrePersist
    public void generateId() {
        if (id == null) {
            this.id = UUID.randomUUID().toString() + userId; // Generate Id;
        }
    }

    
}

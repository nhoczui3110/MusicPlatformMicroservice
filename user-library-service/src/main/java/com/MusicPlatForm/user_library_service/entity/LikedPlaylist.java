package com.MusicPlatForm.user_library_service.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Liked_Playlist")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikedPlaylist {
    @Id
    @Column(name="Id")
    String id;

    @Column(name = "User_id",nullable = false)
    String userId;

    @ManyToOne
    @JoinColumn(name = "Playlist_id",nullable = false)
    Playlist playlist;
    
    @PrePersist
    public void generateId() {
        if (id == null) {
            this.id = UUID.randomUUID().toString()+userId; // Generate Id;
        }
    }
}

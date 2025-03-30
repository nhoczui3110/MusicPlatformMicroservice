package com.MusicPlatForm.comment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "LikedComment")
public class LikedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String likeCommentId;
    @Column(nullable = false)
    String userID;
    @Column(nullable = false)
    LocalDateTime likeAt;
//    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "commentId",  nullable = false)
    Comment comment;
}
package com.MusicPlatForm.comment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String commentId;
    @Column(nullable = false)
    String trackID;
    @Column(nullable = false)
    String userID;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String content;
    @Column(nullable = false)
    LocalDateTime commentAt;
    @Column(nullable = false)
    int likeCount;
    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL)
    List<LikedComment> likedComments;
}

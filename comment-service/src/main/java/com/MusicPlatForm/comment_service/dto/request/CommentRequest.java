package com.MusicPlatForm.comment_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentRequest {
    String commentId;
    String trackID;
    String userID;
    @Size (min = 1, message = "Xin hãy nhập ký tự")
    String content;
    LocalDateTime commentAt = LocalDateTime.now();
    int likeCount;
//    private List<LikedCommentDto> likedComments;
}

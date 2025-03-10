package com.MusicPlatForm.comment_service.dto.response;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRespone {
    String commentId;
    String trackID;
    String userID;
    String content;
    LocalDateTime commentAt = LocalDateTime.now();
    int likeCount;
}

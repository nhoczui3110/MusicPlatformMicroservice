package com.MusicPlatForm.comment_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String trackId;
    String userId;
    String repliedUserId;
    Boolean isLiked;
    String content;
    LocalDateTime commentAt;
    int likeCount;
    List<CommentResponse> replies;
}

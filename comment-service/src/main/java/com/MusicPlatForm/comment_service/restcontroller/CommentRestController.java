package com.MusicPlatForm.comment_service.restcontroller;

import com.MusicPlatForm.comment_service.dto.ApiResponse;
import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentResponse;
import com.MusicPlatForm.comment_service.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentRestController {
    CommentService commentService;

    @PostMapping("/add")
    public ApiResponse<CommentResponse> addComment(@RequestBody @Valid CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Comment added successfully")
                .data(commentService.addComment(request))
                .build();
    }

    @GetMapping("/track/{trackId}")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable String trackId) {
        List<CommentResponse> comments = commentService.getCommentsByTrackId(trackId);
        return ApiResponse.<List<CommentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Success") 
                .data(comments)
                .build();
    }

    @GetMapping("/likes/{commentId}")
    public ApiResponse<Integer> getCommentLikeCount(@PathVariable String commentId) {
        return ApiResponse.<Integer>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(commentService.getCommentLikeCount(commentId))
                .build();
    }

    @PostMapping("/like/{commentId}/{userId}")
    public ApiResponse<Void> likeComment(@PathVariable String commentId, @PathVariable String userId) {
        commentService.likeComment(commentId, userId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Comment liked successfully")
                .build();
    }

    @DeleteMapping("/unlike/{commentId}/{userId}")
    public ApiResponse<Void> unlikeComment(@PathVariable String commentId, @PathVariable String userId) {
        commentService.unlikeComment(commentId, userId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Comment unliked successfully")
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CommentResponse> updateComment(@PathVariable String id, @RequestBody String content) {
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Comment updated successfully")
                .data(commentService.updateComment(id, content))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Comment deleted successfully")
                .build();
    }

    @PostMapping("/reply/{commentId}")
    public ApiResponse<CommentResponse> replyToComment(@PathVariable String commentId, @RequestBody @Valid CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Reply added successfully")
                .data(commentService.replyToComment(commentId, request))
                .build();
    }
}
package com.MusicPlatForm.comment_service.restcontroller;

import com.MusicPlatForm.comment_service.dto.ApiResponse;
import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentRespone;
import com.MusicPlatForm.comment_service.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentRestController {
    CommentService commentService;

    @PostMapping("/add")
    public ApiResponse<CommentRespone> addComment(@RequestBody @Valid CommentRequest request){
        ApiResponse<CommentRespone> apiResponse = new ApiResponse<>();
        apiResponse.setData(commentService.addComment(request));
        return apiResponse;
    }

    @GetMapping("/track/{trackID}")
    public ApiResponse<List<CommentRespone>> getComments(@PathVariable String trackID) {
        ApiResponse<List<CommentRespone>> apiResponse = new ApiResponse<>();
        apiResponse.setData(commentService.getCommentsByTrackId(trackID));
        return apiResponse;
    }

    @PostMapping("/like/{commentID}/{userID}")
    public ApiResponse<Void> likeComment(@PathVariable String commentID, @PathVariable String userID) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        commentService.likeComment(commentID, userID);
        return apiResponse;
    }

    @DeleteMapping("/unlike/{commentID}/{userID}")
    public ApiResponse<Void> unlikeComment(@PathVariable String commentID, @PathVariable String userID) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        commentService.unlikeComment(commentID, userID);
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CommentRespone> updateComment(@PathVariable String id, @RequestBody String content) {
        ApiResponse<CommentRespone> apiResponse = new ApiResponse<>();
        apiResponse.setData(commentService.updateComment(id, content));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable String id) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        commentService.deleteComment(id);
        return apiResponse;
    }
    @PostMapping("/reply/{commentID}")
    public ApiResponse<CommentRespone> replyToComment(@PathVariable String commentID, @RequestBody @Valid CommentRequest request) {
        ApiResponse<CommentRespone> apiResponse = new ApiResponse<>();
        apiResponse.setData(commentService.replyToComment(commentID, request));
        return apiResponse;
    }
}
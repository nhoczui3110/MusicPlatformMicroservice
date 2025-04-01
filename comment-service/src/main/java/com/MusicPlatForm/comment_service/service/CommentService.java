package com.MusicPlatForm.comment_service.service;

import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentResponse;
import com.MusicPlatForm.comment_service.entity.Comment;
import com.MusicPlatForm.comment_service.entity.LikedComment;
import com.MusicPlatForm.comment_service.mapper.CommentMapper;
import com.MusicPlatForm.comment_service.repository.CommentRepository;
import com.MusicPlatForm.comment_service.repository.LikedCommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    LikedCommentRepository likedCommentRepository;
    CommentMapper  commentMapper;

    public CommentResponse addComment(CommentRequest commentRequest) {
        Comment comment = commentMapper.toComment(commentRequest);
        comment.setCommentAt(LocalDateTime.now());
        comment.setLikeCount(0);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public int getCommentLikeCount(String commentId) {
        return likedCommentRepository.countByCommentId(commentId);
    }

    public List<CommentResponse> getCommentsByTrackId(String trackId) {
        List<Comment> comments = commentRepository.findByTrackId(trackId);
        return commentMapper.toCommentResponseList(comments);
    }

    public void likeComment(String commentId, String userId) {
        LikedComment likedComment = new LikedComment(null, userId, LocalDateTime.now(), null);
        likedCommentRepository.save(likedComment);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    public void unlikeComment(String commentId, String userId) {
        LikedComment likedComment = likedCommentRepository.findByCommentIdAndUserId(commentId, userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        likedCommentRepository.delete(likedComment);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);
    }

    public CommentResponse updateComment(String id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(content);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    public CommentResponse replyToComment(String commentId, CommentRequest request) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        Comment replyComment = commentMapper.toComment(request);
        replyComment.setCommentAt(LocalDateTime.now());
        replyComment.setLikeCount(0);

        replyComment = commentRepository.save(replyComment);
        return commentMapper.toCommentResponse(replyComment);
    }
}
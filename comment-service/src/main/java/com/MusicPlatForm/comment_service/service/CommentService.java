package com.MusicPlatForm.comment_service.service;

import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentRespone;
import com.MusicPlatForm.comment_service.entity.Comment;
import com.MusicPlatForm.comment_service.entity.LikedComment;
import com.MusicPlatForm.comment_service.mapper.CommentMapper;
import com.MusicPlatForm.comment_service.repository.CommentRepository;
import com.MusicPlatForm.comment_service.repository.LikedCommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    LikedCommentRepository likedCommentRepository;
    CommentMapper  commentMapper;

    public CommentRespone addComment(CommentRequest commentRequest) {
        Comment comment = commentMapper.toComment(commentRequest);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }


    public List<CommentRespone> getCommentsByTrackId(String trackID) {
        List<Comment> comments = commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getTrackID().equals(trackID))
                .collect(Collectors.toList());
        return commentMapper.toCommentResponseList(comments);
    }

    public void likeComment(String commentID, String userID) {
        LikedComment likedComment = new LikedComment(null, userID, LocalDateTime.now(), null);
        likedCommentRepository.save(likedComment);
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    public void unlikeComment(String commentID, String userID) {
        LikedComment likedComment = likedCommentRepository.findByCommentIDAndUserID(commentID, userID)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        likedCommentRepository.delete(likedComment);

        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);
    }

    public CommentRespone updateComment(String id, String content) {
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

    public CommentRespone replyToComment(String commentID, CommentRequest request) {
        Comment parentComment = commentRepository.findById(commentID)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        Comment replyComment = commentMapper.toComment(request);
        replyComment = commentRepository.save(replyComment);
        return commentMapper.toCommentResponse(replyComment);
    }
}
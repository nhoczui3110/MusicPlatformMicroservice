package com.MusicPlatForm.comment_service.service;

import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentResponse;
import com.MusicPlatForm.comment_service.entity.Comment;
import com.MusicPlatForm.comment_service.entity.LikedComment;
import com.MusicPlatForm.comment_service.exception.AppException;
import com.MusicPlatForm.comment_service.exception.ErrorCode;
import com.MusicPlatForm.comment_service.mapper.CommentMapper;
import com.MusicPlatForm.comment_service.repository.CommentRepository;
import com.MusicPlatForm.comment_service.repository.LikedCommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        comment.setParentComment(null);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public int getCommentLikeCount(String commentId) {
        return likedCommentRepository.countByCommentId(commentId);
    }

    public List<CommentResponse> getCommentsByTrackId(String trackId) {
        List<Comment> comments = commentRepository.findByTrackId(trackId);
        List<CommentResponse> responseList = new ArrayList<>();

        for (Comment parent : comments) {
            CommentResponse parentDto = commentMapper.toCommentResponse(parent);
            List<CommentResponse> replys = new ArrayList<>();
            List<Comment> replies = parent.getReplies();
            if (replies != null) {
                for (Comment reply : replies) {
                    replys.add(commentMapper.toCommentResponse(reply));
                }
            }
            parentDto.setReplies(replys);
            responseList.add(parentDto);
        }
        return responseList;
    }

    public void likeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        LikedComment likedComment = LikedComment.builder()
                .userId(userId)
                .likeAt(LocalDateTime.now())
                .comment(comment)
                .build();
        likedCommentRepository.save(likedComment);
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    public void unlikeComment(String commentId, String userId) {
        List <LikedComment> likedComments = likedCommentRepository.findAllByCommentIdAndUserId(commentId, userId);

        if (likedComments.isEmpty()) {
            throw new AppException(ErrorCode.LIKE_NOT_FOUND);
        }
        likedCommentRepository.deleteAll(likedComments);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        int updatedLikeCount = Math.max(0, comment.getLikeCount() - likedComments.size());
        comment.setLikeCount(updatedLikeCount);

        commentRepository.save(comment);
    }

    public CommentResponse updateComment(String id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setContent(content);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }

    public CommentResponse replyToComment(String commentId, CommentRequest request) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_COMMENT_NOT_FOUND));
        Comment replyComment = commentMapper.toComment(request);
        replyComment.setCommentAt(LocalDateTime.now());
        replyComment.setLikeCount(0);
        replyComment.setTrackId(parentComment.getTrackId());
        replyComment.setParentComment(parentComment);
        replyComment = commentRepository.save(replyComment);
        return commentMapper.toCommentResponse(replyComment);
    }
}
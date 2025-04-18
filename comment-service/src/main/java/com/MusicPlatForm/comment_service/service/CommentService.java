package com.MusicPlatForm.comment_service.service;

import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.request.RepliedCommentRequest;
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Comment comment = commentMapper.toComment(commentRequest);
        comment.setUserId(userId);
        comment.setCommentAt(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setParentComment(null);
        comment = commentRepository.save(comment);
        CommentResponse commentResponse =  commentMapper.toCommentResponse(comment);
        commentResponse.setIsLiked(false);
        return commentResponse;
    }

    public int getCommentLikeCount(String commentId) {
        return likedCommentRepository.countByCommentId(commentId);
    }

    public List<CommentResponse> getCommentsByTrackId(String trackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        List<Comment> comments = commentRepository.findByTrackId(trackId);
        List<CommentResponse> responseList = new ArrayList<>();
        for (Comment parent : comments) {
            CommentResponse parentDto = commentMapper.toCommentResponse(parent);
            List<CommentResponse> replys = new ArrayList<>();
            List<Comment> replies = parent.getReplies();
            parentDto.setIsLiked(false);
            if(this.likedCommentRepository.countByCommentIdAndUserId(parent.getId(), userId)==1){
                parentDto.setIsLiked(true);
            }
            if (replies != null) {
                for (Comment reply : replies) {
                    CommentResponse commentResponse = commentMapper.toCommentResponse(reply);
                    commentResponse.setIsLiked(false);
                    if(this.likedCommentRepository.countByCommentIdAndUserId(reply.getId(), userId)==1){
                        commentResponse.setIsLiked(true);
                    }
                    replys.add(commentResponse);
                }
            }
            parentDto.setReplies(replys);
            responseList.add(parentDto);
        }
        return responseList;
    }

    public void likeComment(String commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
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

    public void unlikeComment(String commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        LikedComment likedComments = likedCommentRepository.findByCommentIdAndUserId(commentId, userId);

        if (likedComments == null) {
            throw new AppException(ErrorCode.LIKE_NOT_FOUND);
        }

        likedCommentRepository.delete(likedComments);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        int updatedLikeCount = Math.max(0, comment.getLikeCount() - 1);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getUserId().equals(userId)) throw new AppException(ErrorCode.UNAUTHORIZED);
        commentRepository.delete(comment);
    }

    @Deprecated
    // xóa
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
    public CommentResponse replyComment(String commentId, RepliedCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        Comment parentComment = comment.getParentComment();
        if(parentComment==null){
            parentComment = comment;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Comment replyComment = commentMapper.toRepliedComment(request);
        replyComment.setTrackId(comment.getTrackId());
        replyComment.setUserId(userId);
        replyComment.setRepliedUserId(comment.getUserId());
        replyComment.setCommentAt(LocalDateTime.now());
        replyComment.setLikeCount(0);
        replyComment.setTrackId(parentComment.getTrackId());
        replyComment.setParentComment(parentComment);
        replyComment = commentRepository.save(replyComment);
        return commentMapper.toCommentResponse(replyComment);
    }
}
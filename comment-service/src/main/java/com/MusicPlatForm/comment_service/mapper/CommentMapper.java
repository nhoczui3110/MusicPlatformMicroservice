package com.MusicPlatForm.comment_service.mapper;

import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.request.LikedCommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentResponse;
import com.MusicPlatForm.comment_service.dto.response.LikedCommentResponse;
import com.MusicPlatForm.comment_service.entity.Comment;
import com.MusicPlatForm.comment_service.entity.LikedComment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    // Comment toComment(CommentRequest commentRequest);
    // @Mapping(source = "id", target = "id")
    // CommentRespone toCommentResponse(Comment comment);

    // List<CommentRespone> toCommentResponseList(List<Comment> comments);

    @Mapping(source = "trackId", target = "trackId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "likeCount", target = "likeCount")
    Comment toComment(CommentRequest commentRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "trackId", target = "trackId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "commentAt", target = "commentAt")
    @Mapping(source = "likeCount", target = "likeCount")
    @Mapping(target = "replies", ignore = true)
    CommentResponse toCommentResponse(Comment comment);

    List<CommentResponse> toCommentResponseList(List<Comment> comments);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "likeAt", target = "likeAt")
    @Mapping(source = "comment.id", target = "commentId")
    LikedCommentResponse toLikedCommentResponse(LikedComment likedComment);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "likeAt", target = "likeAt")
    @Mapping(source = "commentId", target = "comment.id")
    LikedComment toLikedComment(LikedCommentRequest likedCommentRequest);
}

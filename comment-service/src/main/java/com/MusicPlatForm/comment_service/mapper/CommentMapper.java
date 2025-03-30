package com.MusicPlatForm.comment_service.mapper;

import com.MusicPlatForm.comment_service.dto.request.CommentRequest;
import com.MusicPlatForm.comment_service.dto.response.CommentRespone;
import com.MusicPlatForm.comment_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentRequest commentRequest);
    @Mapping(source = "commentId", target = "commentId")
    CommentRespone toCommentResponse(Comment comment);

    List<CommentRespone> toCommentResponseList(List<Comment> comments);
}

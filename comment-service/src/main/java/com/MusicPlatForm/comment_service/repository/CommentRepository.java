package com.MusicPlatForm.comment_service.repository;

import com.MusicPlatForm.comment_service.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,String> {
    List<Comment> findByUserId(String userId);
    List<Comment> findByTrackId(String trackId);
}

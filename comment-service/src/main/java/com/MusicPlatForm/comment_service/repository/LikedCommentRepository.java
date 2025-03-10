package com.MusicPlatForm.comment_service.repository;

import com.MusicPlatForm.comment_service.entity.LikedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface LikedCommentRepository extends JpaRepository<LikedComment,Long> {
    @Query("From Comment where commentId=:commentID and userID=:userID")
    Optional<LikedComment> findByCommentIDAndUserID(String commentID, String userID);


    @Modifying
    @Query("Delete from Comment cmt where cmt.commentId=:commentID and cmt.userID=:userID") //hql
    void deleteByCommentIDAndUserID(String commentID, String userID);
}

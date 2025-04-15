package com.MusicPlatForm.comment_service.repository;

import com.MusicPlatForm.comment_service.entity.LikedComment;

import feign.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedCommentRepository extends JpaRepository<LikedComment,String> {
   
    // @Query("SELECT lc FROM liked_comments lc WHERE lc.comment_id =:comment_id AND lc.user_id =:user_id")
    // Optional<LikedComment> findByCommentIdAndUserId(String commentId, String userId);

    // @Modifying
    // @Query("DELETE FROM liked_comments lc WHERE lc.comment_id =:comment_id AND lc.user_id =:user_id")
    // void deleteByCommentIdAndUserId(String commentId, String userId);
//    @Query("SELECT lc FROM LikedComment lc WHERE lc.comment.id = :commentId AND lc.userId = :userId")
//    Optional<LikedComment> findByCommentIdAndUserId(@Param("commentId") String commentId, @Param("userId") String userId);

    List<LikedComment> findAllByCommentIdAndUserId(String commentId, String userId);

    int countByCommentId(String commentId);

    List<LikedComment> findByCommentId(String commentId);
    List<LikedComment> findByUserId(String userId);
    void deleteByCommentIdAndUserId(String commentId, String userId);
}

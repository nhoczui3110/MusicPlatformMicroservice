package com.MusicPlatForm.notification_service.repository;

import com.MusicPlatForm.notification_service.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);

    @Query("{ '_id': { '$in': ?0 } }")
    List<Notification> findBulkByIds(List<String>ids);

    List<Notification> findAllByRecipientId(String recipientId);
}

package com.MusicPlatForm.notification_service.service;

import com.MusicPlatForm.notification_service.dto.request.EmailResetPasswordRequest;
import com.MusicPlatForm.notification_service.dto.request.NotificationRequest;
import com.MusicPlatForm.notification_service.dto.response.NotificationResponse;
import com.MusicPlatForm.notification_service.entity.Notification;
import com.MusicPlatForm.notification_service.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;
    JavaMailSender mailSender;
    SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotificationWebSocket(NotificationRequest request) {
        Notification notification = Notification.builder()
                .senderId(request.getSenderId())
                .recipientId(request.getRecipientId())
                .message(request.getMessage())
                .trackId(request.getTrackId())
                .commentId(request.getCommentId())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        NotificationResponse response = NotificationResponse.builder()
                .id(notification.getId())
                .senderId(notification.getSenderId())
                .recipientId(notification.getRecipientId())
                .message(notification.getMessage())
                .trackId(notification.getTrackId())
                .commentId(notification.getCommentId())
                .isRead(false)
                .createdAt(notification.getCreatedAt())
                .build();

        simpMessagingTemplate.convertAndSendToUser(
                notification.getRecipientId(),
                "/queue/message",
                response
        );
    }

    public void emailNotification(EmailResetPasswordRequest request) {
        Notification notification = Notification.builder()
                .senderId("system")
                .recipientId(request.getUserEmail())
                .message("Your OTP for password reset: " + request.getOtp())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(request.getUserEmail());
        mailMessage.setSubject("Password Reset OTP");
        mailMessage.setText("Your OTP is: " + request.getOtp());
        mailSender.send(mailMessage);
    }

    // Gửi thông tin tới các service khác qua WebSocket
    private void sendToServiceOther(String destination, NotificationRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", request.getRecipientId());
        payload.put("trackId", request.getTrackId());
        payload.put("senderId", request.getSenderId());
        if (request.getCommentId() != null) {
            payload.put("commentId", request.getCommentId());
        }
        simpMessagingTemplate.convertAndSend(destination, payload);
    }
}

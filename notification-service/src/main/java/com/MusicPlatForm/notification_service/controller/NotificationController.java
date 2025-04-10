package com.MusicPlatForm.notification_service.controller;

import com.MusicPlatForm.notification_service.dto.ApiResponse;
import com.MusicPlatForm.notification_service.dto.request.EmailResetPasswordRequest;
import com.MusicPlatForm.notification_service.dto.request.NotificationRequest;
import com.MusicPlatForm.notification_service.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class NotificationController {
    NotificationService notificationService;
    KafkaTemplate<String, NotificationRequest> notificationKafkaTemplate;
    KafkaTemplate<String, EmailResetPasswordRequest> emailKafkaTemplate;

    // Listener cho thông báo chung
    @KafkaListener(topics = "notification_to_user", groupId = "notification_group")
    public void listenerKafka(NotificationRequest notificationRequest) {
        System.out.println("Received notification: " + notificationRequest);
        notificationService.sendNotificationWebSocket(notificationRequest);
    }

    @KafkaListener(topics = "like", groupId = "notification_group")
    public void likeListener(NotificationRequest request) {
        request.setMessage(request.getSenderId() + " liked your track");
        notificationService.sendNotificationWebSocket(request);
    }

    // Listener cho hành động comment
    @KafkaListener(topics = "comment", groupId = "notification_group")
    public void commentListener(NotificationRequest request) {
        request.setMessage(request.getSenderId() + " commented: " + request.getMessage());
        notificationService.sendNotificationWebSocket(request);
    }

    // Listener cho email (OTP từ identity-service)
    @KafkaListener(topics = "email", groupId = "notification_group")
    public void emailListener(EmailResetPasswordRequest emailResetPasswordRequest) {
        System.out.println("Received email reset password: " + emailResetPasswordRequest);
        notificationService.emailNotification(emailResetPasswordRequest);
    }

    // Gửi thông báo chung
    @PostMapping("/send/general")
    public ApiResponse<String> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationKafkaTemplate.send("notification_to_user", notificationRequest);
        return ApiResponse.<String>builder()
                .code(200)
                .message("General notification sent")
                .data("OK")
                .build();
    }

    // Gửi thông báo email (OTP)
    @PostMapping("/send/email")
    public ApiResponse<String> sendEmailNotification(@RequestBody EmailResetPasswordRequest emailRequest) {
        emailKafkaTemplate.send("email", emailRequest);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Email notification sent")
                .data("OK")
                .build();
    }
}

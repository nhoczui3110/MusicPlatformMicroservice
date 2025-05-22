package com.MusicPlatForm.notification_service.configuration;

import java.security.Principal;
import java.util.Map;

import com.MusicPlatForm.notification_service.model.User;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

// chỉ cho phép user đã đăng mới vào dc
public class UserHandShake extends DefaultHandshakeHandler{
    @PreAuthorize("isAuthenticated()")
    @Override
    protected Principal determineUser(ServerHttpRequest request,WebSocketHandler wsHandler,Map<String, Object> attributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return new User(userId);
    }
}
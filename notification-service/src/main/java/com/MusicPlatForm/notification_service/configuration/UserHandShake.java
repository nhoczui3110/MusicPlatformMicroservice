package com.MusicPlatForm.notification_service.configuration;

import java.security.Principal;
import java.util.Map;

import com.MusicPlatForm.notification_service.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class UserHandShake extends DefaultHandshakeHandler{
    @Override
    protected Principal determineUser(ServerHttpRequest request,WebSocketHandler wsHandler,Map<String, Object> attributes){
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            // thay bằng user id
            String userId = (String) session.getAttribute("userId"); // Giả sử userId được lưu khi login
            if(userId != null)
                return new User(userId);
        }
        return new User("Anonymous");
    }
}
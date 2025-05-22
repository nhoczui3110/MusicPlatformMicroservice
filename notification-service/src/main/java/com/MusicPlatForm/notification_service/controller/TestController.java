package com.MusicPlatForm.notification_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TestController {
    @GetMapping("/a")
    public String getMethodName() {
        return "a";
    }
    
}

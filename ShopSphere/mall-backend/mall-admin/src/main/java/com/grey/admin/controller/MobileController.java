package com.grey.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MobileController {

    @GetMapping("/video")
    public ResponseEntity<Map<String, String>> getVideo() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a video file");
        return ResponseEntity.ok(response);
    }
}
package com.example.Animalgram.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
public class TestController {

    @GetMapping("/hello")
    public String privatehello(){
        return "인증된 사용자";
    }
}

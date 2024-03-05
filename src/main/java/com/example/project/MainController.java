package com.example.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // 기본페이지 요청 메서드
    @GetMapping("/")
    public String index() {
        return "index"; // index.html
    }



}

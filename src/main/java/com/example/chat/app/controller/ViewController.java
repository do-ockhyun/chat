package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.app.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ChatService chatService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/")
    public String chat(@RequestParam String email, Model model, HttpSession session) {
        // 세션에 email 저장
        session.setAttribute("email", email);
        
        List<ChatSessionResponse> chatSessions = chatService.findSessionsByUserId(email);
        log.info("{} chatsessions: {}", email, chatSessions);

        model.addAttribute("email", email);
        model.addAttribute("chatSessions", chatSessions);
        return "chat";
    }
} 
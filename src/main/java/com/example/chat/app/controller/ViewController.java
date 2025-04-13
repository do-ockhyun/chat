package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.app.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ChatService chatService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/")
    public String chat(@RequestParam String email, Model model) {
        List<ChatSessionResponse> sessions = chatService.findSessionsByUserId(email);
        model.addAttribute("email", email);
        model.addAttribute("sessions", sessions);
        return "chat";
    }
} 
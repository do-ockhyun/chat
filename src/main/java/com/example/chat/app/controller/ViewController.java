package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String chat(@RequestParam String empNo, Model model, HttpSession session) {
        // 세션에 empNo 저장
        session.setAttribute("empNo", empNo);
        
        List<ChatSessionResponse> chatSessions = chatService.findSessionsByUserId(empNo);
        log.info("{} chatsessions: {}", empNo, chatSessions);

        model.addAttribute("empNo", empNo);
        model.addAttribute("chatSessions", chatSessions);
        return "chat";
    }

    @GetMapping("/share/{shareToken}")
    public String viewSharedSession(@PathVariable String shareToken, Model model) {
        try {
            ChatSessionResponse session = chatService.findSharedSessionByToken(shareToken);
            java.util.List<ChatResponse> messages = chatService.findMessagesByShareToken(shareToken);
            model.addAttribute("sharedSession", session);
            model.addAttribute("messages", messages);
            return "shared-session";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
} 
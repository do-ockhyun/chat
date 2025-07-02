package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatRequest;
import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.app.dto.NewChatRequest;
import com.example.chat.app.service.ChatService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ApiController {

    private final ChatService chatService;

    @PostMapping("/new")
    public ResponseEntity<Long> createNewChat(
            @Valid @RequestBody NewChatRequest request,
            HttpSession session) {
        // 세션에서 이메일 가져오기
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 요청의 이메일과 세션의 이메일이 일치하는지 확인
        if (!email.equals(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        ChatSessionResponse chatSession = chatService.createSession(request.getEmail(), request.getTitle());
        return ResponseEntity.ok(chatSession.getId());
    }

    @GetMapping("/{sessionId}/messages")
    public ResponseEntity<List<ChatResponse>> getMessages(
            @PathVariable Long sessionId,
            HttpSession session) {
        // 세션에서 이메일 가져오기
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 세션 소유자 확인
        if (!chatService.isSessionOwner(sessionId, email)) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(chatService.findMessagesBySessionId(sessionId));
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<ChatResponse> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody ChatRequest request,
            HttpSession session) {
        // 세션에서 이메일 가져오기
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 세션 소유자 확인
        if (!chatService.isSessionOwner(sessionId, email)) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(chatService.sendMessage(sessionId, request.getContent()));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long sessionId,
            HttpSession session) {
        // 세션에서 이메일 가져오기
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        
        // 세션 소유자 확인
        if (!chatService.isSessionOwner(sessionId, email)) {
            return ResponseEntity.badRequest().build();
        }
        
        chatService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{sessionId}/title")
    public ResponseEntity<Void> updateSessionTitle(
            @PathVariable Long sessionId,
            @RequestBody String title,
            HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!chatService.isSessionOwner(sessionId, email)) {
            return ResponseEntity.badRequest().build();
        }
        chatService.updateSessionTitle(sessionId, title);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{sessionId}/pin")
    public ResponseEntity<Void> updateSessionPin(
            @PathVariable Long sessionId,
            @RequestBody boolean isPinned,
            HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!chatService.isSessionOwner(sessionId, email)) {
            return ResponseEntity.badRequest().build();
        }
        chatService.updateSessionPin(sessionId, isPinned);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionResponse>> getSessions(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        List<ChatSessionResponse> sessions = chatService.findSessionsByUserId(email);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/{sessionId}/share")
    public ResponseEntity<String> shareSession(
            @PathVariable Long sessionId,
            HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String shareToken = chatService.shareSession(sessionId, email);
            return ResponseEntity.ok(shareToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{sessionId}/unshare")
    public ResponseEntity<Void> unshareSession(
            @PathVariable Long sessionId,
            HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            chatService.unshareSession(sessionId, email);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 
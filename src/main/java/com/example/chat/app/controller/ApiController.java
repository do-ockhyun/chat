package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatRequest;
import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.dto.NewChatRequest;
import com.example.chat.app.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ChatService chatService;

    @PostMapping("/new")
    public ResponseEntity<Long> createNewChat(@Valid @RequestBody NewChatRequest request) {
        return ResponseEntity.ok(chatService.createSession(request.getEmail(), request.getTitle()).getId());
    }

    @GetMapping("/{sessionId}/messages")
    public ResponseEntity<List<ChatResponse>> getMessages(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.findMessagesBySessionId(sessionId));
    }

    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<ChatResponse> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(sessionId, request.getContent()));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        chatService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }
} 
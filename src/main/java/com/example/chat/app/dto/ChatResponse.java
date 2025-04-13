package com.example.chat.app.dto;

import com.example.chat.domain.entity.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponse {
    private Long id;
    private String content;
    private MessageType messageType;
    private LocalDateTime createdAt;

    public ChatResponse(Long id, String content, MessageType messageType, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.messageType = messageType;
        this.createdAt = createdAt;
    }
} 
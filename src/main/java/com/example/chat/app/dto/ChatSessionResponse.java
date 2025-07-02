package com.example.chat.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatSessionResponse {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonProperty("isPinned")
    private boolean isPinned;
    @JsonProperty("isShared")
    private boolean isShared;
    private String shareToken;
    private LocalDateTime lastMessageAt;

    public ChatSessionResponse(Long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isPinned, boolean isShared, String shareToken, LocalDateTime lastMessageAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPinned = isPinned;
        this.isShared = isShared;
        this.shareToken = shareToken;
        this.lastMessageAt = lastMessageAt;
    }
} 
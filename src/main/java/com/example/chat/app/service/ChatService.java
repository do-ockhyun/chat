package com.example.chat.app.service;

import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.dto.ChatSessionResponse;
import java.util.List;

public interface ChatService {
    // 채팅 세션 관련
    ChatSessionResponse createSession(String email, String title);
    List<ChatSessionResponse> findSessionsByUserId(String userId);
    void deleteSession(Long sessionId);
    boolean isSessionOwner(Long sessionId, String email);
    void updateSessionTitle(Long sessionId, String title);
    void updateSessionPin(Long sessionId, boolean isPinned);

    // 채팅 메시지 관련
    ChatResponse sendMessage(Long sessionId, String content);
    List<ChatResponse> findMessagesBySessionId(Long sessionId);
    List<ChatResponse> findMessagesByShareToken(String shareToken);

    // 공유 관련
    String shareSession(Long sessionId, String email);
    void unshareSession(Long sessionId, String email);
    ChatSessionResponse findSharedSessionByToken(String shareToken);
} 
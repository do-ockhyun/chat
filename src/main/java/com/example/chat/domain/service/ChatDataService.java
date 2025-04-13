package com.example.chat.domain.service;

import com.example.chat.domain.entity.ChatMessage;
import com.example.chat.domain.entity.ChatSession;
import com.example.chat.domain.entity.MessageType;
import com.example.chat.domain.repository.ChatMessageRepository;
import com.example.chat.domain.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatDataService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅 세션 관련 메서드
    @Transactional
    public ChatSession createSession(String userId, String title) {
        ChatSession session = new ChatSession(userId, title);
        return chatSessionRepository.save(session);
    }

    public List<ChatSession> findSessionsByUserId(String userId) {
        return chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        chatSessionRepository.deleteById(sessionId);
    }

    // 채팅 메시지 관련 메서드
    @Transactional
    public ChatMessage saveUserMessage(Long sessionId, String content) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        ChatMessage message = new ChatMessage(session, content, MessageType.USER);
        return chatMessageRepository.save(message);
    }

    @Transactional
    public ChatMessage saveSystemMessage(Long sessionId, String content) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        ChatMessage message = new ChatMessage(session, content, MessageType.SYSTEM);
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> findMessagesBySessionId(Long sessionId) {
        return chatMessageRepository.findByChatSessionIdOrderByCreatedAtAsc(sessionId);
    }

    public ChatSession findSessionById(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
    }
} 
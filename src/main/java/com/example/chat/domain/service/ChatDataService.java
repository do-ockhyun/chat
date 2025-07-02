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
@Transactional
public class ChatDataService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅 세션 관련 메서드
    public ChatSession createSession(String userId, String title) {
        ChatSession session = new ChatSession(userId, title);
        return chatSessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public List<ChatSession> findSessionsByUserId(String userId) {
        return chatSessionRepository.findByUserIdAndIsDeletedFalseOrderByIsPinnedDescUpdatedAtDesc(userId);
    }

    public void deleteSession(Long sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setDeleted(true);
        chatSessionRepository.save(session);
    }

    public void updateSessionTitle(Long sessionId, String title) {
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setTitle(title);
        chatSessionRepository.save(session);
    }

    public void updateSessionPin(Long sessionId, boolean isPinned) {
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setPinned(isPinned);
        chatSessionRepository.save(session);
    }

    // 채팅 메시지 관련 메서드
    public ChatMessage saveUserMessage(Long sessionId, String content) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        ChatMessage message = new ChatMessage(session, content, MessageType.USER);
        return chatMessageRepository.save(message);
    }

    public ChatMessage saveSystemMessage(Long sessionId, String content) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        ChatMessage message = new ChatMessage(session, content, MessageType.SYSTEM);
        return chatMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findMessagesBySessionId(Long sessionId) {
        return chatMessageRepository.findByChatSessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Transactional(readOnly = true)
    public ChatSession findSessionById(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
    }
} 
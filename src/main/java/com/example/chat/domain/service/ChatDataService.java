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
        return chatSessionRepository.findRecentByUserIdLastMsg(userId);
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

    public ChatSession shareSession(Long sessionId, String shareToken) {
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setShared(true);
        session.setShareToken(shareToken);
        session.setSharedAt(java.time.LocalDateTime.now());
        return chatSessionRepository.save(session);
    }

    public ChatSession unshareSession(Long sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setShared(false);
        session.setShareToken(null);
        session.setSharedAt(null);
        return chatSessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public ChatSession findSharedSessionByToken(String shareToken) {
        return chatSessionRepository.findByShareToken(shareToken);
    }

    // 채팅 메시지 관련 메서드
    public ChatMessage saveUserMessage(Long sessionId, String content) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        ChatMessage message = new ChatMessage(session, content, MessageType.USER);
        session.setLastMessageAt(java.time.LocalDateTime.now());
        chatSessionRepository.save(session);
        return chatMessageRepository.save(message);
    }

    public ChatMessage saveSystemMessage(Long sessionId, String content) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        ChatMessage message = new ChatMessage(session, content, MessageType.SYSTEM);
        session.setLastMessageAt(java.time.LocalDateTime.now());
        chatSessionRepository.save(session);
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

    @Transactional(readOnly = true)
    public List<ChatMessage> findMessagesByShareToken(String shareToken) {
        ChatSession session = findSharedSessionByToken(shareToken);
        if (session == null) throw new IllegalArgumentException("공유 세션이 존재하지 않습니다.");
        return chatMessageRepository.findByChatSessionIdOrderByCreatedAtAsc(session.getId());
    }
} 
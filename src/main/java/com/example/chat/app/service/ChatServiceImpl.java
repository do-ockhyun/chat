package com.example.chat.app.service;

import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.domain.entity.ChatMessage;
import com.example.chat.domain.entity.ChatSession;
import com.example.chat.domain.service.ChatDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatDataService chatDataService;
    private final OpenAiService openAiService;

    @Override
    public ChatSessionResponse createSession(String email, String title) {
        ChatSession session = chatDataService.createSession(email, title);
        return convertToSessionResponse(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatSessionResponse> findSessionsByUserId(String userId) {
        return chatDataService.findSessionsByUserId(userId).stream()
                .map(this::convertToSessionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSession(Long sessionId) {
        chatDataService.deleteSession(sessionId);
    }

    @Override
    public ChatResponse sendMessage(Long sessionId, String content) {
        // 사용자 메시지 저장
        ChatMessage userMessage = chatDataService.saveUserMessage(sessionId, content);
        
        // OpenAI API 호출하여 응답 생성
        String gptResponse = openAiService.generateResponse(content);
        
        // GPT 응답 저장
        ChatMessage systemMessage = chatDataService.saveSystemMessage(sessionId, gptResponse);
        
        return convertToChatResponse(systemMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponse> findMessagesBySessionId(Long sessionId) {
        return chatDataService.findMessagesBySessionId(sessionId).stream()
                .map(this::convertToChatResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionOwner(Long sessionId, String email) {
        try {
            ChatSession session = chatDataService.findSessionById(sessionId);
            return session.getUserId().equals(email);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void updateSessionTitle(Long sessionId, String title) {
        chatDataService.updateSessionTitle(sessionId, title);
    }

    @Override
    public void updateSessionPin(Long sessionId, boolean isPinned) {
        chatDataService.updateSessionPin(sessionId, isPinned);
    }

    private ChatSessionResponse convertToSessionResponse(ChatSession session) {
        return new ChatSessionResponse(
                session.getId(),
                session.getTitle(),
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }

    private ChatResponse convertToChatResponse(ChatMessage message) {
        return new ChatResponse(
                message.getId(),
                message.getContent(),
                message.getMessageType(),
                message.getCreatedAt()
        );
    }
} 
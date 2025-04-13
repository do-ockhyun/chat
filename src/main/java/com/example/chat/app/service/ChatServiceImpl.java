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
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatDataService chatDataService;

    @Override
    @Transactional
    public ChatSessionResponse createSession(String email, String title) {
        ChatSession session = chatDataService.createSession(email, title);
        return convertToSessionResponse(session);
    }

    @Override
    public List<ChatSessionResponse> findSessionsByUserId(String userId) {
        return chatDataService.findSessionsByUserId(userId).stream()
                .map(this::convertToSessionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSession(Long sessionId) {
        chatDataService.deleteSession(sessionId);
    }

    @Override
    @Transactional
    public ChatResponse sendMessage(Long sessionId, String content) {
        // 사용자 메시지 저장
        ChatMessage userMessage = chatDataService.saveUserMessage(sessionId, content);
        
        // TODO: GPT API 호출 및 응답 처리
        String gptResponse = "GPT 응답 예시"; // 임시 응답
        
        // GPT 응답 저장
        ChatMessage systemMessage = chatDataService.saveSystemMessage(sessionId, gptResponse);
        
        return convertToChatResponse(systemMessage);
    }

    @Override
    public List<ChatResponse> findMessagesBySessionId(Long sessionId) {
        return chatDataService.findMessagesBySessionId(sessionId).stream()
                .map(this::convertToChatResponse)
                .collect(Collectors.toList());
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
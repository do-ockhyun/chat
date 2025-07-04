package com.example.chat.app.service;

import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.domain.entity.ChatMessage;
import com.example.chat.domain.entity.ChatSession;
import com.example.chat.domain.entity.MessageType;
import com.example.chat.domain.service.ChatDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatDataService chatDataService;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    @DisplayName("새로운 채팅 세션을 생성할 수 있다")
    void createSession() {
        // given
        String email = "test@example.com";
        String title = "Test Chat";
        ChatSession session = new ChatSession(email, title);
        given(chatDataService.createSession(email, title)).willReturn(session);

        // when
        ChatSessionResponse response = chatService.createSession(email, title);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("사용자의 채팅 세션 목록을 조회할 수 있다")
    void findSessionsByUserId() {
        // given
        String userId = "test@example.com";
        ChatSession session1 = new ChatSession(userId, "Chat 1");
        ChatSession session2 = new ChatSession(userId, "Chat 2");
        List<ChatSession> sessions = Arrays.asList(session1, session2);
        given(chatDataService.findSessionsByUserId(userId)).willReturn(sessions);

        // when
        List<ChatSessionResponse> responses = chatService.findSessionsByUserId(userId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getTitle()).isEqualTo("Chat 1");
        assertThat(responses.get(1).getTitle()).isEqualTo("Chat 2");
    }

    @Test
    @DisplayName("채팅 세션을 삭제할 수 있다")
    void deleteSession() {
        // given
        Long sessionId = 1L;

        // when
        chatService.deleteSession(sessionId);

        // then
        verify(chatDataService).deleteSession(sessionId);
    }

    @Test
    @DisplayName("메시지를 전송하고 GPT 응답을 받을 수 있다")
    void sendMessage() {
        // given
        Long sessionId = 1L;
        String content = "Hello";
        ChatSession session = new ChatSession("test@example.com", "Test Chat");
        ChatMessage userMessage = new ChatMessage(session, content, MessageType.USER);
        ChatMessage systemMessage = new ChatMessage(session, "GPT 응답", MessageType.SYSTEM);
        
        given(chatDataService.saveUserMessage(eq(sessionId), eq(content))).willReturn(userMessage);
        given(chatDataService.saveSystemMessage(eq(sessionId), any())).willReturn(systemMessage);

        // when
        ChatResponse response = chatService.sendMessage(sessionId, content);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMessageType()).isEqualTo(MessageType.SYSTEM);
    }

    @Test
    @DisplayName("세션의 메시지 목록을 조회할 수 있다")
    void findMessagesBySessionId() {
        // given
        Long sessionId = 1L;
        ChatSession session = new ChatSession("test@example.com", "Test Chat");
        ChatMessage message1 = new ChatMessage(session, "Hello", MessageType.USER);
        ChatMessage message2 = new ChatMessage(session, "Hi", MessageType.SYSTEM);
        List<ChatMessage> messages = Arrays.asList(message1, message2);
        given(chatDataService.findMessagesBySessionId(sessionId)).willReturn(messages);

        // when
        List<ChatResponse> responses = chatService.findMessagesBySessionId(sessionId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getMessageType()).isEqualTo(MessageType.USER);
        assertThat(responses.get(1).getMessageType()).isEqualTo(MessageType.SYSTEM);
    }
} 
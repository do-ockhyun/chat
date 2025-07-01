package com.example.chat.domain.service;

import com.example.chat.domain.entity.ChatMessage;
import com.example.chat.domain.entity.ChatSession;
import com.example.chat.domain.entity.MessageType;
import com.example.chat.domain.repository.ChatMessageRepository;
import com.example.chat.domain.repository.ChatSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatDataServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @InjectMocks
    private ChatDataService chatDataService;

    @Test
    @DisplayName("새로운 채팅 세션을 생성할 수 있다")
    void createSession() {
        // given
        String userId = "test@example.com";
        String title = "Test Chat";
        ChatSession expectedSession = new ChatSession(userId, title);
        given(chatSessionRepository.save(any(ChatSession.class))).willReturn(expectedSession);

        // when
        ChatSession result = chatDataService.createSession(userId, title);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getTitle()).isEqualTo(title);
        verify(chatSessionRepository).save(any(ChatSession.class));
    }

    @Test
    @DisplayName("사용자의 채팅 세션 목록을 조회할 수 있다")
    void findSessionsByUserId() {
        // given
        String userId = "test@example.com";
        ChatSession session1 = new ChatSession(userId, "Chat 1");
        ChatSession session2 = new ChatSession(userId, "Chat 2");
        List<ChatSession> expectedSessions = Arrays.asList(session1, session2);
        given(chatSessionRepository.findByUserIdAndIsDeletedFalseOrderByUpdatedAtDesc(userId)).willReturn(expectedSessions);

        // when
        List<ChatSession> result = chatDataService.findSessionsByUserId(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(session1, session2);
    }

    @Test
    @DisplayName("채팅 세션을 삭제할 수 있다")
    void deleteSession() {
        // given
        Long sessionId = 1L;

        // when
        chatDataService.deleteSession(sessionId);

        // then
        verify(chatSessionRepository).deleteById(sessionId);
    }

    @Test
    @DisplayName("사용자 메시지를 저장할 수 있다")
    void saveUserMessage() {
        // given
        Long sessionId = 1L;
        String content = "Hello";
        ChatSession session = new ChatSession("test@example.com", "Test Chat");
        given(chatSessionRepository.findById(sessionId)).willReturn(Optional.of(session));
        given(chatMessageRepository.save(any(ChatMessage.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ChatMessage result = chatDataService.saveUserMessage(sessionId, content);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getMessageType()).isEqualTo(MessageType.USER);
        assertThat(result.getChatSession()).isEqualTo(session);
    }

    @Test
    @DisplayName("시스템 메시지를 저장할 수 있다")
    void saveSystemMessage() {
        // given
        Long sessionId = 1L;
        String content = "GPT Response";
        ChatSession session = new ChatSession("test@example.com", "Test Chat");
        given(chatSessionRepository.findById(sessionId)).willReturn(Optional.of(session));
        given(chatMessageRepository.save(any(ChatMessage.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ChatMessage result = chatDataService.saveSystemMessage(sessionId, content);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getMessageType()).isEqualTo(MessageType.SYSTEM);
        assertThat(result.getChatSession()).isEqualTo(session);
    }

    @Test
    @DisplayName("존재하지 않는 세션에 메시지를 저장하면 예외가 발생한다")
    void saveMessageWithInvalidSession() {
        // given
        Long sessionId = 1L;
        given(chatSessionRepository.findById(sessionId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> chatDataService.saveUserMessage(sessionId, "Hello"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Session not found");
    }

    @Test
    @DisplayName("세션의 메시지 목록을 조회할 수 있다")
    void findMessagesBySessionId() {
        // given
        Long sessionId = 1L;
        ChatSession session = new ChatSession("test@example.com", "Test Chat");
        ChatMessage message1 = new ChatMessage(session, "Hello", MessageType.USER);
        ChatMessage message2 = new ChatMessage(session, "Hi", MessageType.SYSTEM);
        List<ChatMessage> expectedMessages = Arrays.asList(message1, message2);
        given(chatMessageRepository.findByChatSessionIdOrderByCreatedAtAsc(sessionId)).willReturn(expectedMessages);

        // when
        List<ChatMessage> result = chatDataService.findMessagesBySessionId(sessionId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(message1, message2);
    }
} 
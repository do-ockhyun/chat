package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatRequest;
import com.example.chat.app.dto.ChatResponse;
import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.app.dto.NewChatRequest;
import com.example.chat.app.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    private static final String TEST_EMAIL = "user@example.com";

    @Test
    @DisplayName("새로운 채팅 세션을 생성한다")
    void createNewChat() throws Exception {
        // given
        NewChatRequest request = new NewChatRequest();
        request.setEmail(TEST_EMAIL);
        request.setTitle("Test Chat");
        ChatSessionResponse response = new ChatSessionResponse(1L, "Test Chat", null, null, false, false, null, null);
        given(chatService.createSession(eq(request.getEmail()), eq(request.getTitle())))
                .willReturn(response);

        // 세션 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", TEST_EMAIL);

        // when & then
        mockMvc.perform(post("/api/chat/new")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    @DisplayName("채팅 메시지를 조회한다")
    void getMessages() throws Exception {
        // given
        Long sessionId = 1L;
        ChatResponse message1 = new ChatResponse(1L, "Hello", null, null);
        ChatResponse message2 = new ChatResponse(2L, "Hi", null, null);
        given(chatService.findMessagesBySessionId(sessionId))
                .willReturn(Arrays.asList(message1, message2));
        given(chatService.isSessionOwner(eq(sessionId), eq(TEST_EMAIL)))
                .willReturn(true);

        // 세션 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", TEST_EMAIL);

        // when & then
        mockMvc.perform(get("/api/chat/{sessionId}/messages", sessionId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Hello"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].content").value("Hi"));
    }

    @Test
    @DisplayName("메시지를 전송한다")
    void sendMessage() throws Exception {
        // given
        Long sessionId = 1L;
        ChatRequest request = new ChatRequest();
        request.setContent("Hello");
        ChatResponse response = new ChatResponse(1L, "GPT Response", null, null);
        given(chatService.sendMessage(eq(sessionId), eq(request.getContent())))
                .willReturn(response);
        given(chatService.isSessionOwner(eq(sessionId), eq(TEST_EMAIL)))
                .willReturn(true);

        // 세션 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", TEST_EMAIL);

        // when & then
        mockMvc.perform(post("/api/chat/{sessionId}/messages", sessionId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("GPT Response"));
    }

    @Test
    @DisplayName("채팅 세션을 삭제한다")
    void deleteSession() throws Exception {
        // given
        Long sessionId = 1L;
        given(chatService.isSessionOwner(eq(sessionId), eq(TEST_EMAIL)))
                .willReturn(true);

        // 세션 설정
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", TEST_EMAIL);

        // when & then
        mockMvc.perform(delete("/api/chat/{sessionId}", sessionId)
                        .session(session))
                .andExpect(status().isOk());

        verify(chatService).deleteSession(sessionId);
    }
} 
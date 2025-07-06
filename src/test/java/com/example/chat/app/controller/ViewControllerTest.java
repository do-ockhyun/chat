package com.example.chat.app.controller;

import com.example.chat.app.dto.ChatSessionResponse;
import com.example.chat.app.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ViewController.class)
class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Test
    @DisplayName("인덱스 페이지를 렌더링한다")
    void index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("사번을 입력하면 채팅 페이지로 이동한다")
    void chat() throws Exception {
        // given
        String empNo = "EMP001";
        ChatSessionResponse session1 = new ChatSessionResponse(1L, "Chat 1", null, null, false, false, null, null);
        ChatSessionResponse session2 = new ChatSessionResponse(2L, "Chat 2", null, null, false, false, null, null);
        given(chatService.findSessionsByUserId(empNo))
                .willReturn(Arrays.asList(session1, session2));

        // when & then
        mockMvc.perform(post("/")
                        .param("empNo", empNo))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"))
                .andExpect(model().attribute("empNo", empNo))
                .andExpect(model().attributeExists("chatSessions"));
    }
} 
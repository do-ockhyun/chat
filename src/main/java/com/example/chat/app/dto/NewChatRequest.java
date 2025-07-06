package com.example.chat.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewChatRequest {
    @NotBlank(message = "사번을 입력해주세요")
    private String empNo;

    @NotBlank(message = "대화방 제목을 입력해주세요")
    private String title;
} 
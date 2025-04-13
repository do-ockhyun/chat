package com.example.chat.app.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {
    private final ChatLanguageModel chatModel;

    public OpenAiService(@Value("${openai.api-key}") String apiKey) {
        this.chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-3.5-turbo")
                .build();
    }

    public String generateResponse(String userMessage) {
        return chatModel.generate(userMessage);
    }
} 
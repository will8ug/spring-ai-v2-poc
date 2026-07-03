package io.will.springai2poc.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiModelConfiguration {
    @Bean
    public ChatModel defaultFlashModel() {
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                        .apiKey("sk-ba0d38fcda7d4251a5fb622ce453fe87")
                        .model("qwen3.6-flash")
                        .build())
                .build();
    }
}

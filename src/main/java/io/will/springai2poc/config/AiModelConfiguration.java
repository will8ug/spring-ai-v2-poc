package io.will.springai2poc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiModelConfiguration {
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return ChatClient.builder(chatModel)
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
    }
}

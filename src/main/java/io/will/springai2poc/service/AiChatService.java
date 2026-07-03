package io.will.springai2poc.service;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiChatService {
    private final ChatModel defaultChatModel;

    public AiChatService(ChatModel defaultFlashModel) {
        this.defaultChatModel = defaultFlashModel;
    }

    public Mono<CustomChatResponse> sendMessage(CustomChatRequest request) {
        ChatClient chatClient = ChatClient.builder(defaultChatModel).build();
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(request.query())
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic())
                .map(CustomChatResponse::new);
    }

    public Flux<CustomChatResponse> streamMessage(CustomChatRequest request) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        ChatClient chatClient = ChatClient.builder(defaultChatModel).build();

        return chatClient.prompt()
                .advisors(chatMemoryAdvisor)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
                .user(request.query())
                .stream()
                .content()
                .map(CustomChatResponse::new);
    }
}

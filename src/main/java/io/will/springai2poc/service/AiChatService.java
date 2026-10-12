package io.will.springai2poc.service;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiChatService {
    private final ChatClient chatClient;

    public AiChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Mono<CustomChatResponse> sendMessage(CustomChatRequest request) {
        return Mono.fromCallable(() -> chatClient.prompt()
                        .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
                        .user(request.query())
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic())
                .map(CustomChatResponse::new);
    }

    public Flux<CustomChatResponse> streamMessage(CustomChatRequest request) {
        return chatClient.prompt()
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, request.conversationId()))
                .user(request.query())
                .stream()
                .content()
                .map(CustomChatResponse::new);
    }
}

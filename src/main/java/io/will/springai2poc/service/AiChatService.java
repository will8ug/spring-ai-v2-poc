package io.will.springai2poc.service;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiChatService {
    private final ChatClient chatClient;

    public AiChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public Mono<CustomChatResponse> sendMessage(CustomChatRequest request) {
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(request.query())
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic())
                .map(CustomChatResponse::new);
    }

    public Flux<CustomChatResponse> streamMessage(CustomChatRequest request) {
        return chatClient.prompt()
                .user(request.query())
                .stream()
                .content()
                .map(CustomChatResponse::new);
    }
}

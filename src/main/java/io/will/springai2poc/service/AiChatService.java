package io.will.springai2poc.service;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AiChatService {
    public Mono<CustomChatResponse> sendMessage(CustomChatRequest request) {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                        .apiKey("sk-ba0d38fcda7d4251a5fb622ce453fe87")
                        .model("qwen3.6-flash")
                        .build())
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return Mono.fromCallable(() -> chatClient.prompt()
                        .user(request.query())
                        .call()
                        .content())
                .subscribeOn(Schedulers.boundedElastic())
                .map(CustomChatResponse::new);
    }

    public Flux<CustomChatResponse> streamMessage(CustomChatRequest request) {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                        .apiKey("sk-ba0d38fcda7d4251a5fb622ce453fe87")
                        .model("qwen3.6-flash")
                        .build())
                .build();

        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return chatClient.prompt()
                .user(request.query())
                .stream()
                .content()
                .map(CustomChatResponse::new);
    }
}

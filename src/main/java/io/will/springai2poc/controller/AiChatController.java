package io.will.springai2poc.controller;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import io.will.springai2poc.service.AiChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AiChatController {
    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CustomChatResponse> chat(@RequestBody CustomChatRequest request) {
        return aiChatService.sendMessage(request);
    }

    @PostMapping(path = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomChatResponse> chatStream(@RequestBody CustomChatRequest request) {
        return aiChatService.streamMessage(request);
    }
}

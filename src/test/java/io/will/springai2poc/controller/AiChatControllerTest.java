package io.will.springai2poc.controller;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import io.will.springai2poc.service.AiChatService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(AiChatController.class)
class AiChatControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AiChatService aiChatService;

    @Test
    void chatStreamShouldGenerateUuidWhenConversationIdIsBlank() {
        when(aiChatService.streamMessage(org.mockito.ArgumentMatchers.any(CustomChatRequest.class)))
                .thenReturn(Flux.just(new CustomChatResponse("Hello")));

        webTestClient.post()
                .uri("/chat/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CustomChatRequest.withQuery("Hello!"))
                .exchange()
                .expectStatus().isOk();

        ArgumentCaptor<CustomChatRequest> requestCaptor = ArgumentCaptor.forClass(CustomChatRequest.class);
        verify(aiChatService).streamMessage(requestCaptor.capture());

        CustomChatRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.conversationId())
                .isNotBlank()
                .satisfies(UUID::fromString);
    }
}

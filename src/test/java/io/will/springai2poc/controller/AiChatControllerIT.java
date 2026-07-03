package io.will.springai2poc.controller;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AiChatControllerIT {
    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void beforeEach() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void testChat() {
        webTestClient.post()
                .uri("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CustomChatRequest.withQuery("Hello!"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomChatResponse.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.content());
                    assertFalse(response.content().isBlank());
                });
    }

    @Test
    void testChatStream() {
        Flux<CustomChatResponse> responses = webTestClient.post()
                .uri("/chat/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CustomChatRequest("Hello!", "test-conversation-01"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .returnResult(CustomChatResponse.class)
                .getResponseBody();

        AtomicInteger fluxCount = new AtomicInteger();
        StepVerifier.create(responses)
                .thenConsumeWhile(response -> {
                    fluxCount.incrementAndGet();
                    return response.content() != null && !response.content().isBlank();
                })
                .verifyComplete();

        assertTrue(fluxCount.get() > 0);
        System.out.println(fluxCount.get());
    }
}

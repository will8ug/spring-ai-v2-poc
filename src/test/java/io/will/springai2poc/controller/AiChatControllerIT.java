package io.will.springai2poc.controller;

import io.will.springai2poc.controller.model.CustomChatRequest;
import io.will.springai2poc.controller.model.CustomChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                .bodyValue(new CustomChatRequest("Hello!"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomChatResponse.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.content());
                    assertFalse(response.content().isBlank());
                });
    }
}

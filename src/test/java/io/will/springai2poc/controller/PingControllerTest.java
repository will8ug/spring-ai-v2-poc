package io.will.springai2poc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(PingController.class)
class PingControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void pingReturnsPong() {
        webTestClient
            .get().uri("/ping")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("PONG");
    }
}

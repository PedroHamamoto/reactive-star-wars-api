package br.com.hamamoto.starwars.planet.view;

import br.com.hamamoto.starwars.BaseTest;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

class PlanetEndpointTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnOkAndCreateAPlanet() {
        var request = getRequest("fixtures/planets/requests/new-planet.json", PlanetCreationRequest.class);

        webTestClient.post()
                .uri("/rs/planets")
                .body(Mono.just(request), PlanetCreationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Tatooine")
                .jsonPath("$.climate").isEqualTo("arid")
                .jsonPath("$.terrain").isEqualTo("desert")
                .jsonPath("$.appearances-quantity").isEqualTo(0);
    }

}
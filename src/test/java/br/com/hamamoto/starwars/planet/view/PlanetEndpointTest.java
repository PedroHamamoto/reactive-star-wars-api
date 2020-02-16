package br.com.hamamoto.starwars.planet.view;

import br.com.hamamoto.starwars.BaseTest;
import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainExceptionMessage;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

class PlanetEndpointTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnOkAndCreateAPlanet() {
        var request = getRequest("fixtures/planets/requests/new-planet.json", PlanetCreationRequest.class);

        stubFor(get(urlEqualTo("/planets?search=Tatooine"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(SC_OK)
                        .withBodyFile("swapi/planets/tatooine-search-response.json")));

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
                .jsonPath("$.appearances-quantity").isEqualTo(5);
    }

    @Test
    public void shouldReturnBadRequestWhenItsNotAStarWarsPlanet() {
        var request = getRequest("fixtures/planets/requests/not-a-star-wars-planet.json", PlanetCreationRequest.class);

        stubFor(get(urlEqualTo("/planets?search=Krypton"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(SC_OK)
                        .withBodyFile("swapi/planets/krypton-search-response.json")));

        webTestClient.post()
                .uri("/rs/planets")
                .body(Mono.just(request), PlanetCreationRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(DomainExceptionMessage.NOT_A_STAR_WARS_PLANET.getCode())
                .jsonPath("$.message").isEqualTo(DomainExceptionMessage.NOT_A_STAR_WARS_PLANET.getMessage());
    }

}
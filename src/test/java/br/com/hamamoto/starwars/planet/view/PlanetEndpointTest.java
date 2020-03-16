package br.com.hamamoto.starwars.planet.view;

import br.com.hamamoto.starwars.BaseTest;
import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainExceptionMessage;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreateRequest;
import br.com.hamamoto.starwars.planet.view.resource.PlanetUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static wiremock.org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;

class PlanetEndpointTest extends BaseTest {

    public static final String EXISTENT_PLANET_ID = "507f191e810c19729de860ea";
    public static final String NON_EXISTENT_PLANET_ID = "non-existent-planet-id";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnOkAndCreateAPlanet() {
        var request = getRequest("fixtures/planets/requests/new-planet.json", PlanetCreateRequest.class);

        stubFor(get(urlEqualTo("/planets?search=Tatooine"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(SC_OK)
                        .withBodyFile("swapi/planets/tatooine-search-response.json")));

        webTestClient.post()
                .uri("/rs/planets")
                .body(Mono.just(request), PlanetCreateRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Tatooine")
                .jsonPath("$.climate").isEqualTo("arid")
                .jsonPath("$.terrain").isEqualTo("desert")
                .jsonPath("$.appearances-quantity").isEqualTo(5);
    }

    @Test
    public void shouldReturnBadRequestWhenItsNotAStarWarsPlanet() {
        var request = getRequest("fixtures/planets/requests/not-a-star-wars-planet.json", PlanetCreateRequest.class);

        stubFor(get(urlEqualTo("/planets?search=Krypton"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(SC_OK)
                        .withBodyFile("swapi/planets/krypton-search-response.json")));

        webTestClient.post()
                .uri("/rs/planets")
                .body(Mono.just(request), PlanetCreateRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(DomainExceptionMessage.NOT_A_STAR_WARS_PLANET.getCode())
                .jsonPath("$.message").isEqualTo(DomainExceptionMessage.NOT_A_STAR_WARS_PLANET.getMessage());
    }

    @Test
    public void shouldReturnNoContentAndUpdateAPlanet() {
        var request = getRequest("fixtures/planets/requests/update-planet.json", PlanetUpdateRequest.class);

        insertPlanet("data/planets/tatooine.json");

        webTestClient
                .put()
                .uri("/rs/planets/" + EXISTENT_PLANET_ID)
                .body(Mono.just(request), PlanetUpdateRequest.class)
                .exchange()
                .expectStatus().isNoContent();

        var existingPlanet = findPlanetById(EXISTENT_PLANET_ID);

        assertThat(existingPlanet.getId(), is(EXISTENT_PLANET_ID));
        assertThat(existingPlanet.getAppearancesQuantity(), is(request.getAppearancesQuantity()));
        assertThat(existingPlanet.getClimate(), is(request.getClimate()));
        assertThat(existingPlanet.getName(), is(request.getName()));
        assertThat(existingPlanet.getTerrain(), is(request.getTerrain()));
    }

    @Test
    public void shouldReturnNotFoundWhenAttemptToUpdateANonExistentPlanet() {
        var request = getRequest("fixtures/planets/requests/update-planet.json", PlanetUpdateRequest.class);

        webTestClient
                .put()
                .uri("/rs/planets/" + NON_EXISTENT_PLANET_ID)
                .body(Mono.just(request), PlanetUpdateRequest.class)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(DomainExceptionMessage.PLANET_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(DomainExceptionMessage.PLANET_NOT_FOUND.getMessage());
    }

    @Test
    public void shouldReturnOkWhenGetAExistentPlanet() {
        insertPlanet("data/planets/tatooine.json");

        webTestClient
                .get()
                .uri("/rs/planets/" + EXISTENT_PLANET_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(EXISTENT_PLANET_ID)
                .jsonPath("$.name").isEqualTo("Tatooine")
                .jsonPath("$.climate").isEqualTo("arid")
                .jsonPath("$.terrain").isEqualTo("desert")
                .jsonPath("$.appearances-quantity").isEqualTo(5);

    }

}
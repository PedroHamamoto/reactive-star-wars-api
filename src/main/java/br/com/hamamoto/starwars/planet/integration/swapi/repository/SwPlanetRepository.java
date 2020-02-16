package br.com.hamamoto.starwars.planet.integration.swapi.repository;

import br.com.hamamoto.starwars.planet.integration.swapi.SwAPiConfiguration;
import br.com.hamamoto.starwars.planet.integration.swapi.resource.SwApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SwPlanetRepository {

    private final WebClient webClient;
    private final SwAPiConfiguration swAPiConfiguration;

    public Mono<SwApiResponse> findAllByName(String name) {
        return webClient.get()
                .uri(String.format("%s/planets?search=%s", swAPiConfiguration.getSwapiUrl(), name))
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(SwApiResponse.class));
    }
}

package br.com.hamamoto.starwars.planet.service;

import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainException;
import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainExceptionMessage;
import br.com.hamamoto.starwars.planet.entity.Planet;
import br.com.hamamoto.starwars.planet.integration.swapi.repository.SwPlanetRepository;
import br.com.hamamoto.starwars.planet.integration.swapi.resource.SwApiResponse;
import br.com.hamamoto.starwars.planet.integration.swapi.resource.SwPlanetResponse;
import br.com.hamamoto.starwars.planet.repository.PlanetRepository;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreateRequest;
import br.com.hamamoto.starwars.planet.view.resource.PlanetUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final SwPlanetRepository swPlanetRepository;

    public Mono<Planet> create(PlanetCreateRequest request) {
        return swPlanetRepository.findAllByName(request.getName())
                .map(SwApiResponse::getResults)
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Mono.error(new DomainException(DomainExceptionMessage.NOT_A_STAR_WARS_PLANET)))
                .filter(swPlanetResponse -> swPlanetResponse.getName().equalsIgnoreCase(request.getName())).take(1L).single()
                .map(swPlanetResponse -> toEntity(swPlanetResponse, request))
                .flatMap(planetRepository::save);
    }

    public Mono<Planet> update(PlanetUpdateRequest request, String id) {
        return planetRepository.findById(id)
                .switchIfEmpty(Mono.error(new DomainException(DomainExceptionMessage.PLANET_NOT_FOUND)))
                .map(planet -> toUpdateEntity(request, id))
                .flatMap(planetRepository::save);
    }

    public Planet toUpdateEntity(PlanetUpdateRequest request, String id) {
        return Planet.builder()
                .withId(id)
                .withName(request.getName())
                .withTerrain(request.getTerrain())
                .withClimate(request.getClimate())
                .withAppearancesQuantity(request.getAppearancesQuantity())
                .build();
    }

    private Planet toEntity(SwPlanetResponse swPlanetResponse, PlanetCreateRequest request) {
        return Planet.builder()
                .withName(request.getName())
                .withClimate(request.getClimate())
                .withTerrain(request.getTerrain())
                .withAppearancesQuantity(swPlanetResponse.getFilms().size())
                .build();
    }
}

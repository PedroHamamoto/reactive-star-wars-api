package br.com.hamamoto.starwars.planet.service;

import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainException;
import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainExceptionMessage;
import br.com.hamamoto.starwars.planet.entity.Planet;
import br.com.hamamoto.starwars.planet.integration.swapi.repository.SwPlanetRepository;
import br.com.hamamoto.starwars.planet.integration.swapi.resource.SwPlanetResponse;
import br.com.hamamoto.starwars.planet.repository.PlanetRepository;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final SwPlanetRepository swPlanetRepository;

    public Mono<Planet> create(PlanetCreationRequest request) {
       return swPlanetRepository.findAllByName(request.getName())
               .map(response -> response.getResults())
               .flatMapMany(Flux::fromIterable)
               .switchIfEmpty(Mono.error(new DomainException(DomainExceptionMessage.NOT_A_STAR_WARS_PLANET)))
               .filter(swPlanetResponse -> swPlanetResponse.getName().equalsIgnoreCase(request.getName())).take(1L).single()
               .map(swPlanetResponse -> toEntity(swPlanetResponse, request))
               .flatMap(planetRepository::save);
    }

    private Planet toEntity(SwPlanetResponse swPlanetResponse, PlanetCreationRequest request) {
        return Planet.builder()
                .withName(request.getName())
                .withClimate(request.getClimate())
                .withTerrain(request.getClimate())
                .withAppearancesQuantity(swPlanetResponse.getFilms().size())
                .build();
    }

}

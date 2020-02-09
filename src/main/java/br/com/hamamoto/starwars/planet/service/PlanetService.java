package br.com.hamamoto.starwars.planet.service;

import br.com.hamamoto.starwars.planet.entity.Planet;
import br.com.hamamoto.starwars.planet.repository.PlanetRepository;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    public Mono<Planet> create(PlanetCreationRequest request) {
        return planetRepository.save(toEntity(request));
    }

    private Planet toEntity(PlanetCreationRequest request) {
        return new Planet(request.getName(), request.getClimate(), request.getTerrain(), 0);
    }
}

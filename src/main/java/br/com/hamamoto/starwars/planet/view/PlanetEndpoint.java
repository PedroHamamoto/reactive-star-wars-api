package br.com.hamamoto.starwars.planet.view;

import br.com.hamamoto.starwars.planet.entity.Planet;
import br.com.hamamoto.starwars.planet.service.PlanetService;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreationRequest;
import br.com.hamamoto.starwars.planet.view.resource.PlanetResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/rs/planets")
public class PlanetEndpoint {

    private final PlanetService service;

    @PostMapping
    public Mono<PlanetResponse> create(@Valid @RequestBody PlanetCreationRequest request) {
        return service.create(request)
                .map(this::toResource);
    }

    private PlanetResponse toResource(Planet planet) {
        return new PlanetResponse(planet.getId(), planet.getName(), planet.getClimate(), planet.getTerrain(),
                planet.getAppearancesQuantity());
    }

}

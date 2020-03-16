package br.com.hamamoto.starwars.planet.view;

import br.com.hamamoto.starwars.planet.entity.Planet;
import br.com.hamamoto.starwars.planet.service.PlanetService;
import br.com.hamamoto.starwars.planet.view.resource.PlanetCreateRequest;
import br.com.hamamoto.starwars.planet.view.resource.PlanetResponse;
import br.com.hamamoto.starwars.planet.view.resource.PlanetUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/rs/planets")
public class PlanetEndpoint {

    private final PlanetService service;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Mono<PlanetResponse> create(@Valid @RequestBody PlanetCreateRequest request) {
        return service.create(request)
                .map(this::toResponse);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public Mono<PlanetResponse> update(@Valid @RequestBody PlanetUpdateRequest request, @PathVariable("id") String id) {
        return service.update(request, id)
                .map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<PlanetResponse> getById(@PathVariable("id") String id) {
        return service.getById(id)
                .map(this::toResponse);
    }

    private PlanetResponse toResponse(Planet planet) {
        return PlanetResponse.builder()
                .withId(planet.getId())
                .withName(planet.getName())
                .withClimate(planet.getClimate())
                .withTerrain(planet.getTerrain())
                .withAppearancesQuantity(planet.getAppearancesQuantity())
                .build();
    }

}

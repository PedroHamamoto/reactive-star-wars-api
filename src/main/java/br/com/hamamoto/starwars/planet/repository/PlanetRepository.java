package br.com.hamamoto.starwars.planet.repository;

import br.com.hamamoto.starwars.planet.entity.Planet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlanetRepository extends ReactiveCrudRepository<Planet, String> {
}

package br.com.hamamoto.starwars.planet.integration.swapi.resource;

import lombok.*;

import java.util.List;

@Data
public class SwPlanetResponse {
    private String name;
    private String climate;
    private String terrain;
    private List<String> films;
}

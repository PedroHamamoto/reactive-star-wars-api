package br.com.hamamoto.starwars.planet.view.resource;

import lombok.*;

@Data
public class PlanetCreationRequest {
    private String name;
    private String climate;
    private String terrain;
}

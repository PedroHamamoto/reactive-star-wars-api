package br.com.hamamoto.starwars.planet.view.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PlanetCreationRequest {

    private String name;
    private String climate;
    private String terrain;

}

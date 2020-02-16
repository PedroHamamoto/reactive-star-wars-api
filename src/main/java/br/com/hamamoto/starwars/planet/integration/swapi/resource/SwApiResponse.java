package br.com.hamamoto.starwars.planet.integration.swapi.resource;

import lombok.Data;

import java.util.List;

@Data
public class SwApiResponse {
    private int count;
    private String next;
    private String previous;
    private List<SwPlanetResponse> results;

}

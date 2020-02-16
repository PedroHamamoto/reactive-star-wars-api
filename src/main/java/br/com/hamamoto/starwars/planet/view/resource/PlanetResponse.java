package br.com.hamamoto.starwars.planet.view.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PlanetResponse {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    @JsonProperty("appearances-quantity")
    private int appearancesQuantity;

}
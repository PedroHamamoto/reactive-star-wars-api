package br.com.hamamoto.starwars.planet.view.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder(setterPrefix = "with")
public class PlanetResponse {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    @JsonProperty("appearances-quantity")
    private int appearancesQuantity;

}
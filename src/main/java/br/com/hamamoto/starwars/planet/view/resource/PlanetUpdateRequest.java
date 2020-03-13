package br.com.hamamoto.starwars.planet.view.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlanetUpdateRequest {

    private String name;
    private String climate;
    private String terrain;
    @JsonProperty("appearances-quantity")
    private int appearancesQuantity;

}

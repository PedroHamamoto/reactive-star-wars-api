package br.com.hamamoto.starwars.planet.integration.swapi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SwAPiConfiguration {

    @Value("${swapi.url}")
    private String swapiUrl;
}

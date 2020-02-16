package br.com.hamamoto.starwars.infrastructure.exception.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResource {

    public int code;
    public String message;

}

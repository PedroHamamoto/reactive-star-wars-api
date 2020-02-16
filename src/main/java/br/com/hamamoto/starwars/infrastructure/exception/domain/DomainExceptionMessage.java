package br.com.hamamoto.starwars.infrastructure.exception.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@Getter
public enum DomainExceptionMessage {

    NOT_A_STAR_WARS_PLANET("That's not a Star Wars Planet", BAD_REQUEST, 400001),
    UNKNOWN_ERROR("An unkown error has ocurred, please contact the dev team", INTERNAL_SERVER_ERROR, 500001);

    private final String message;
    private final HttpStatus httpStatus;
    private final int code;
}

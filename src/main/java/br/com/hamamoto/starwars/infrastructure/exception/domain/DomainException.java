package br.com.hamamoto.starwars.infrastructure.exception.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final int code;

    public DomainException(DomainExceptionMessage domainExceptionMessage) {
        super(domainExceptionMessage.getMessage());

        this.httpStatus = domainExceptionMessage.getHttpStatus();
        this.code = domainExceptionMessage.getCode();
    }
}

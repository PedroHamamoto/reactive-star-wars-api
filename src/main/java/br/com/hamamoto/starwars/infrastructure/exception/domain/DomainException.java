package br.com.hamamoto.starwars.infrastructure.exception.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

    private HttpStatus httpStatus;
    private int code;

    public DomainException(DomainExceptionMessage domainExceptionMessage) {
        super(domainExceptionMessage.getMessage());

        this.httpStatus = domainExceptionMessage.getHttpStatus();
        this.code = domainExceptionMessage.getCode();
    }
}

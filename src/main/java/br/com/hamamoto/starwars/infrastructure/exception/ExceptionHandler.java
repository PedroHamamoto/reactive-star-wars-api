package br.com.hamamoto.starwars.infrastructure.exception;

import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainException;
import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ExceptionHandler implements WebExceptionHandler {

    private final ExceptionParser exceptionParser;

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

        Optional<DomainException> domainExceptionOptional = Optional.ofNullable(throwable)
                .filter(DomainException.class::isInstance)
                .map(DomainException.class::cast);

        if (domainExceptionOptional.isPresent()) {
            DomainException domainException = domainExceptionOptional.get();
            return Mono.from(exceptionParser.parse(domainException, serverWebExchange));
        }

        log.error("Unknown error {}", throwable);

        return Mono.from(exceptionParser.parse(new DomainException(DomainExceptionMessage.UNKNOWN_ERROR), serverWebExchange));
    }
}

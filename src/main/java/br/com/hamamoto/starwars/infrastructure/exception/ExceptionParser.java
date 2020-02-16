package br.com.hamamoto.starwars.infrastructure.exception;

import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainException;
import br.com.hamamoto.starwars.infrastructure.exception.domain.ErrorResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class ExceptionParser {

    private final ObjectMapper mapper;
    private final DataBufferFactory dataBufferFactory;

    public Publisher<Void> parse(DomainException domainException, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(domainException.getHttpStatus());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Mono<DataBuffer> body = null;
        try {
            body = Mono.just(dataBufferFactory.wrap(mapper.writeValueAsBytes(new ErrorResource(domainException.getCode(), domainException.getMessage()))));
        } catch (JsonProcessingException e) {
            log.error("Failed to parse {}", domainException.toString());
        }
        return exchange.getResponse().writeWith(body);
    }

}

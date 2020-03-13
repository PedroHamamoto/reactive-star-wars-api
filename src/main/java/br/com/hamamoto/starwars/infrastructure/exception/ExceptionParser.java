package br.com.hamamoto.starwars.infrastructure.exception;

import br.com.hamamoto.starwars.infrastructure.exception.domain.DomainException;
import br.com.hamamoto.starwars.infrastructure.exception.domain.ErrorResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
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

    @SneakyThrows
    public Publisher<Void> parse(DomainException domainException, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(domainException.getHttpStatus());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Mono<DataBuffer> body = null;
        body = Mono.just(dataBufferFactory.wrap(mapper.writeValueAsBytes(new ErrorResource(domainException.getCode(), domainException.getMessage()))));

        return exchange.getResponse().writeWith(body);
    }

}

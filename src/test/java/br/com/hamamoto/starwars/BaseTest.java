package br.com.hamamoto.starwars;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
public class BaseTest {

    @Autowired
    private ObjectMapper mapper;

    private final String RESOURCE_DIRECTORY = "src/test/resources/";

    @SneakyThrows
    public String resource(String path) {
        File file = new File(RESOURCE_DIRECTORY + path);

        if (!file.exists()) {
            throw new IllegalArgumentException("Payload file not found: " + RESOURCE_DIRECTORY + path);
        }

        return new String(Files.readAllBytes(Paths.get(file.getPath())));
    }

    @SneakyThrows
    public <T> T getRequest(String path, Class<T> clazz) {
        return mapper.readValue(resource(path), clazz);
    }
}

package br.com.hamamoto.starwars;

import br.com.hamamoto.starwars.planet.entity.Planet;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String RESOURCE_DIRECTORY = "src/test/resources/";

    @SneakyThrows
    public String resource(String path) {
        File file = new File(RESOURCE_DIRECTORY + path);

        if (!file.exists()) {
            throw new IllegalArgumentException("Payload file not found: " + RESOURCE_DIRECTORY + path);
        }

        return new String(Files.readAllBytes(Paths.get(file.getPath())));
    }

    public void insertPlanet(String path) {
        mongoTemplate.insert(Document.parse(resource(path)), "planets");
    }

    public Planet findPlanetById(String id) {
        return mongoTemplate.findById(new ObjectId("507f191e810c19729de860ea"), Planet.class);
    }

    @SneakyThrows
    public <T> T getRequest(String path, Class<T> clazz) {
        return mapper.readValue(resource(path), clazz);
    }
}

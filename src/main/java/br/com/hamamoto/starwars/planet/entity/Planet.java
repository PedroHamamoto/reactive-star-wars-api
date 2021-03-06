package br.com.hamamoto.starwars.planet.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@NoArgsConstructor
@Builder(setterPrefix = "with")
@Document("planets")
public class Planet {

    @Id
    private String id;
    @NonNull  private String name;
    @NonNull private String climate;
    @NonNull private String terrain;
    @Field("appearances-quantity")
    @NonNull private int appearancesQuantity;

}

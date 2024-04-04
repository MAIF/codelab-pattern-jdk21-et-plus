package fr.maif.patternjava.appv2.domain.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = Adresse.AdresseBtoB.class,
                name = "AdresseBtoB"
        ),
        @JsonSubTypes.Type(
                value = Adresse.AdresseBtoC.class,
                name = "AdresseBtoC"
        )}
)
public sealed interface Adresse {
        @Builder(toBuilder = true)
        record AdresseBtoB(
                String ligne1,
                String ligne2,
                String ligne3,
                String ligne4,
                String ligne5,
                String ligne6,
                String ligne7
        ) implements Adresse {

        }

        @Builder(toBuilder = true)
        record AdresseBtoC(
                String ligne1,
                String ligne2,
                String ligne3,
                String ligne4,
                String ligne5,
                String ligne6,
                String ligne7
        ) implements Adresse {
        }
}

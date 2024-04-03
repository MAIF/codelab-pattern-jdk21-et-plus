package fr.maif.patternjava.appv1.domain.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Adresse {
        @NotNull
        public TypeAdresse type;
        @NotNull
        public String ligne1;
        public String ligne2;
        public String ligne3;
        @NotNull
        public String ligne4;
        public String ligne5;
        @NotNull
        public String ligne6;
        public String ligne7;

}

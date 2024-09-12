package fr.maif.patternjava.app.domain.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public sealed interface Colis {

    sealed interface ColisExistant extends Colis {
        String reference();
    }

    @Builder
    record NouveauColis(LocalDateTime dateDEnvoi, @NotNull @Email String email, @NotNull @Valid Adresse adresse) implements Colis {

        public NouveauColis {
            dateDEnvoi = Objects.requireNonNullElse(dateDEnvoi, LocalDateTime.now());
        }

        public ColisPrisEnCharge toColisPrisEnCharge(String reference) {
            return new ColisPrisEnCharge(reference, dateDEnvoi, email, adresse);
        }
    }

    record ColisPrisEnCharge(@NotNull String reference, @NotNull LocalDateTime dateDEnvoi, @NotNull @Email String email, @NotNull @Valid Adresse adresse) implements ColisExistant {

    }

    record ColisEnCoursDAcheminement(@NotNull String reference, @NotNull LocalDateTime dateDEnvoi, @NotNull Double latitude, @NotNull Double longitude, @NotNull @Email String email, @NotNull @Valid Adresse adresse) implements ColisExistant {

    }

    @Builder
    record ColisRecu(@NotNull String reference, @NotNull LocalDateTime dateDEnvoi, @NotNull LocalDateTime dateDeReception, @NotNull @Email String email,
                     @NotNull @Valid Adresse adresse) implements ColisExistant {

    }
}





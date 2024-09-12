package fr.maif.patternjava.app.domain.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Builder;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
public record Colis(
        String reference,
        @NotNull TypeColis type,
        @NotNull LocalDateTime dateDEnvoi,
        LocalDateTime dateDeReception,
        Double latitude,
        Double longitude,
        @Email @NotNull String email,
        @Valid Adresse adresse) {

    public Colis {
        dateDEnvoi = Objects.requireNonNullElse(dateDEnvoi, LocalDateTime.now());
    }
}





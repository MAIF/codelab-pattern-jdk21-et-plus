package fr.maif.patternjava.app.domain.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

import static fr.maif.patternjava.app.domain.models.Validation.*;


public sealed interface ColisOuErreur {

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(
                    value = Colis.NouveauColis.class,
                    name = "NouveauColis"
            ),
            @JsonSubTypes.Type(
                    value = Colis.ColisPrisEnCharge.class,
                    name = "ColisPrisEnCharge"
            ),
            @JsonSubTypes.Type(
                    value = Colis.ColisEnCoursDAcheminement.class,
                    name = "ColisEnCoursDAcheminement"
            ),
            @JsonSubTypes.Type(
                    value = ColisOuErreur.ColisRecu.class,
                    name = "ColisRecu"
            )}
    )
    sealed interface Colis extends ColisOuErreur {
    }

    sealed interface ColisExistant extends Colis {
        ReferenceColis reference();
    }

    sealed interface ColisInvalide extends ColisOuErreur {
        String message();
    }

    record ColisNonTrouve(String message) implements ColisInvalide {
    }

    record EtatInvalide(String message) implements ColisInvalide {
    }


    @Builder
    record NouveauColis(LocalDateTime dateDEnvoi,
                        Email email,
                        Adresse adresse) implements Colis {

        public NouveauColis {
            dateDEnvoi = Objects.requireNonNullElse(dateDEnvoi, LocalDateTime.now());
            throwInvalid(nonNull(dateDEnvoi, "dateDEnvoi")
                    .and(nonNull(email, "email"))
                    .and(nonNull(adresse, "adresse"))
            );
        }

        public ColisPrisEnCharge toColisPrisEnCharge(ReferenceColis reference) {
            return new ColisPrisEnCharge(reference, dateDEnvoi, email, adresse);
        }
    }

    record ColisPrisEnCharge(ReferenceColis reference,
                             LocalDateTime dateDEnvoi,
                             Email email,
                             Adresse adresse) implements ColisExistant {
        public ColisPrisEnCharge {
            throwInvalid(nonNull(reference, "reference")
                    .and(nonNull(dateDEnvoi, "dateDEnvoi"))
                    .and(nonNull(email, "email"))
                    .and(nonNull(adresse, "adresse"))
            );
        }
    }

    record ColisEnCoursDAcheminement(ReferenceColis reference,
                                     LocalDateTime dateDEnvoi,
                                     Double latitude,
                                     Double longitude,
                                     Email email,
                                     Adresse adresse) implements ColisExistant {
        public ColisEnCoursDAcheminement {
            throwInvalid(nonNull(reference, "reference")
                    .and(nonNull(dateDEnvoi, "dateDEnvoi"))
                    .and(nonNull(latitude, "latitude"))
                    .and(nonNull(longitude, "longitude"))
                    .and(nonNull(email, "email"))
                    .and(nonNull(adresse, "adresse"))
            );
        }
    }

    @Builder
    record ColisRecu(ReferenceColis reference,
                     LocalDateTime dateDEnvoi,
                     LocalDateTime dateDeReception,
                     Email email,
                     Adresse adresse) implements ColisExistant {
        public ColisRecu {
            throwInvalid(nonNull(reference, "reference")
                    .and(nonNull(dateDEnvoi, "dateDEnvoi"))
                    .and(nonNull(email, "email"))
                    .and(nonNull(adresse, "adresse"))
            );
        }
    }

    record ReferenceColis(@JsonValue String value) { }

    record Email(@JsonValue String email) {
        public Email {
            throwInvalid(nonNull(email).andThen(() -> emailValid(email)));
        }
    }
}

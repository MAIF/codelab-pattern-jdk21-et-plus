package fr.maif.patternjava.appv2.domain.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;


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
     sealed interface Colis extends ColisOuErreur {}

     sealed interface ColisExistant extends Colis {
         String reference();
     }

     sealed interface ColisInvalide extends ColisOuErreur {
         String message();
     }

     record ColisNonTrouve(String message) implements ColisInvalide { }
     record EtatInvalide(String message) implements ColisInvalide { }

     @Builder
     record NouveauColis(LocalDateTime dateDEnvoi, @NotNull @Email String email, @NotNull Adresse adresse) implements Colis {

         public NouveauColis {
             dateDEnvoi = Objects.requireNonNullElse(dateDEnvoi, LocalDateTime.now());
         }

         public ColisPrisEnCharge toColisPrisEnCharge(String reference) {
             return new ColisPrisEnCharge(reference, dateDEnvoi, email, adresse);
         }
     }

     record ColisPrisEnCharge(@NotNull String reference, @NotNull LocalDateTime dateDEnvoi, @NotNull @Email String email, @NotNull Adresse adresse) implements ColisExistant {

     }

     record ColisEnCoursDAcheminement(@NotNull String reference, @NotNull LocalDateTime dateDEnvoi, @NotNull Double latitude, @NotNull Double longitude, @NotNull @Email String email, @NotNull Adresse adresse) implements ColisExistant {

     }

     @Builder
     record ColisRecu(@NotNull String reference, @NotNull LocalDateTime dateDEnvoi, @NotNull LocalDateTime dateDeReception, @NotNull @Email String email,
                      @NotNull Adresse adresse) implements ColisExistant {

     }

 }





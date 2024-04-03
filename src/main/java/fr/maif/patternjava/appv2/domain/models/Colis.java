package fr.maif.patternjava.appv2.domain.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;


 public sealed interface Colis {

     sealed interface ColisExistant extends Colis {
         String reference();
     }
     @Builder
     record NouveauColis(@NotNull LocalDateTime dateDEnvoi, @NotNull @Email String email, @NotNull Adresse adresse) implements Colis {

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





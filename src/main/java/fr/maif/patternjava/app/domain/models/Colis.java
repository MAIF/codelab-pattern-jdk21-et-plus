package fr.maif.patternjava.app.domain.models;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

 @Data
 @Builder(toBuilder = true)
 public class Colis {
     public String reference;
     @NotNull
     public TypeColis type;
     @NotNull
     public LocalDateTime dateDEnvoi;
     public LocalDateTime dateDeReception;
     public Double latitude;
     public Double longitude;
     @Email
     @NotNull
     public String email;
     public Adresse adresse;

     public Colis(String reference, TypeColis type, LocalDateTime dateDEnvoi, LocalDateTime dateDeReception, Double latitude, Double longitude, String email, Adresse adresse) {
         this.reference = reference;
         this.type = type;
         this.dateDEnvoi = Objects.requireNonNullElse(dateDEnvoi, LocalDateTime.now());
         this.dateDeReception = dateDeReception;
         this.latitude = latitude;
         this.longitude = longitude;
         this.email = email;
         this.adresse = adresse;
     }
 }





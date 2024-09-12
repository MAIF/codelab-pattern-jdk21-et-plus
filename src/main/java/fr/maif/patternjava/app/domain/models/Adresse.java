package fr.maif.patternjava.app.domain.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Data
@Builder(toBuilder = true)
public class Adresse {
        @NotNull
        public TypeAdresse type;
        // Commun
        @Length(max = 38) String entreeBatimentImmeubleResidence;
        @NotNull @Length(max = 38) String numeroLibelleVoie;
        @NotNull @Length(max = 38) String codePostalEtLocaliteOuCedex;
        @Length(max = 38) String pays;

        // B2B
        @Length(max = 38) String raisonSocialeOuDenomination;
        @Length(max = 38) String identiteDestinataireOuService;
        @Length(max = 38) String mentionSpecialeEtCommuneGeo;

        // B2C
        @Length(max = 38) String civiliteNomPrenom;
        @Length(max = 38) String noAppEtageCouloirEscalier;
        @Length(max = 38) String lieuDitServiceParticulierDeDistribution;

        public Adresse(TypeAdresse type, String entreeBatimentImmeubleResidence, String numeroLibelleVoie, String codePostalEtLocaliteOuCedex, String pays, String raisonSocialeOuDenomination, String identiteDestinataireOuService, String mentionSpecialeEtCommuneGeo, String civiliteNomPrenom, String noAppEtageCouloirEscalier, String lieuDitServiceParticulierDeDistribution) {
                this.type = type;
                this.entreeBatimentImmeubleResidence = entreeBatimentImmeubleResidence;
                this.numeroLibelleVoie = numeroLibelleVoie;
                this.codePostalEtLocaliteOuCedex = codePostalEtLocaliteOuCedex;
                this.pays = pays;

                if (type == TypeAdresse.AdresseBtoB) {
                        Objects.requireNonNull(raisonSocialeOuDenomination, "required pour une adresse B2B");
                        Objects.requireNonNull(identiteDestinataireOuService, "required pour une adresse B2B");
                } else {
                        Objects.requireNonNull(civiliteNomPrenom, "required pour une adresse B2C");
                }
                this.raisonSocialeOuDenomination = raisonSocialeOuDenomination;
                this.identiteDestinataireOuService = identiteDestinataireOuService;
                this.mentionSpecialeEtCommuneGeo = mentionSpecialeEtCommuneGeo;

                this.civiliteNomPrenom = civiliteNomPrenom;
                this.noAppEtageCouloirEscalier = noAppEtageCouloirEscalier;
                this.lieuDitServiceParticulierDeDistribution = lieuDitServiceParticulierDeDistribution;
        }
}

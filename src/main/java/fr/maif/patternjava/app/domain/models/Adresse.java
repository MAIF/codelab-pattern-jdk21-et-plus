package fr.maif.patternjava.app.domain.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

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
                @NotNull @Length(max = 38) String raisonSocialeOuDenomination,
                @NotNull @Length(max = 38) String identiteDestinataireOuService,
                @Length(max = 38) String mentionSpecialeEtCommuneGeo,
                @Length(max = 38) String entreeBatimentImmeubleResidence,
                @NotNull @Length(max = 38) String numeroLibelleVoie,
                @NotNull @Length(max = 38) String codePostalEtLocaliteOuCedex,
                @Length(max = 38) String pays
        ) implements Adresse { }

        @Builder(toBuilder = true)
        record AdresseBtoC(
                @NotNull @Length(max = 38) String civiliteNomPrenom,
                @Length(max = 38) String noAppEtageCouloirEscalier,
                @Length(max = 38) String lieuDitServiceParticulierDeDistribution,
                @Length(max = 38) String entreeBatimentImmeubleResidence,
                @NotNull @Length(max = 38) String numeroLibelleVoie,
                @NotNull @Length(max = 38) String codePostalEtLocaliteOuCedex,
                @Length(max = 38) String pays
        ) implements Adresse { }
}
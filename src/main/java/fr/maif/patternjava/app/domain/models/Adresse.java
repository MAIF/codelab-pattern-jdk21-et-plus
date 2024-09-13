package fr.maif.patternjava.app.domain.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;

import static fr.maif.patternjava.app.domain.models.Validation.*;

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
            RaisonSocialeOuDenomination raisonSocialeOuDenomination,
            IdentiteDestinataireOuService identiteDestinataireOuService,
            MentionSpecialeEtCommuneGeo mentionSpecialeEtCommuneGeo,
            EntreeBatimentImmeubleResidence entreeBatimentImmeubleResidence,
            NumeroLibelleVoie numeroLibelleVoie,
            CodePostalEtLocaliteOuCedex codePostalEtLocaliteOuCedex,
            Pays pays
    ) implements Adresse {
        public AdresseBtoB {
            throwInvalid(nonNull(raisonSocialeOuDenomination, "raisonSocialeOuDenomination")
                    .and(nonNull(identiteDestinataireOuService, "identiteDestinataireOuService"))
                    .and(nonNull(numeroLibelleVoie, "numeroLibelleVoie"))
                    .and(nonNull(codePostalEtLocaliteOuCedex, "codePostalEtLocaliteOuCedex"))
                    .and(nonNull(pays, "pays"))
            );
        }
    }

    @Builder(toBuilder = true)
    record AdresseBtoC(
            CiviliteNomPrenom civiliteNomPrenom,
            NoAppEtageCouloirEscalier noAppEtageCouloirEscalier,
            LieuDitServiceParticulierDeDistribution lieuDitServiceParticulierDeDistribution,
            EntreeBatimentImmeubleResidence entreeBatimentImmeubleResidence,
            NumeroLibelleVoie numeroLibelleVoie,
            CodePostalEtLocaliteOuCedex codePostalEtLocaliteOuCedex,
            Pays pays
    ) implements Adresse {
        public AdresseBtoC {
            throwInvalid(nonNull(civiliteNomPrenom, "civiliteNomPrenom")
                    .and(nonNull(numeroLibelleVoie, "numeroLibelleVoie"))
                    .and(nonNull(codePostalEtLocaliteOuCedex, "codePostalEtLocaliteOuCedex"))
                    .and(nonNull(pays, "pays"))
            );
        }
    }

    static String formatterLigneAdresse(String ligne) {
        return ligne.toUpperCase()
                .replaceAll("\\.", " ");
    }

    record NoAppEtageCouloirEscalier(@JsonValue String value) {
        public NoAppEtageCouloirEscalier {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
        }
    }

    record EntreeBatimentImmeubleResidence(@JsonValue String value) {
        public EntreeBatimentImmeubleResidence {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
        }
    }

    record NumeroLibelleVoie(@JsonValue String value) {
        public NumeroLibelleVoie(String value) {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
            this.value = formatterLigneAdresse(value);
        }
    }

    record LieuDitServiceParticulierDeDistribution(@JsonValue String value) {
        public LieuDitServiceParticulierDeDistribution(String value) {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
            this.value = formatterLigneAdresse(value);
        }
    }

    record CodePostalEtLocaliteOuCedex(@JsonValue String value) {
        public CodePostalEtLocaliteOuCedex(String value) {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
            this.value = formatterLigneAdresse(value);
        }
    }

    record Pays(@JsonValue String value) {
        public Pays(String value) {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
            this.value = formatterLigneAdresse(value);
        }
    }

    record RaisonSocialeOuDenomination(@JsonValue String value) {
        public RaisonSocialeOuDenomination {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
        }
    }

    record IdentiteDestinataireOuService(@JsonValue String value) {
        public IdentiteDestinataireOuService {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
        }
    }

    record MentionSpecialeEtCommuneGeo(@JsonValue String value) {
        public MentionSpecialeEtCommuneGeo(String value) {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
            this.value = formatterLigneAdresse(value);
        }
    }

    record CiviliteNomPrenom(@JsonValue String value) {
        public CiviliteNomPrenom {
            throwInvalid(nonNull(value).andThen(() ->
                    tailleMax(value, 38)
            ));
        }
    }

}
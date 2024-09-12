package fr.maif.patternjava.app.domain;

import fr.maif.patternjava.app.domain.errors.ColisNonTrouve;
import fr.maif.patternjava.app.domain.errors.EtatInvalide;
import fr.maif.patternjava.app.domain.models.Colis;
import fr.maif.patternjava.app.domain.models.TypeColis;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static fr.maif.patternjava.app.domain.models.TypeColis.*;

@Component("LivraisonDeColisClassic")
public class LivraisonDeColis {

    public final ColisExistants colisExistants;

    public LivraisonDeColis(ColisExistants colisExistants) {
        this.colisExistants = colisExistants;
    }

    public List<Colis> listerColis() {
        return this.colisExistants.listerColis();
    }


    public Colis prendreEnChargeLeColis(Colis colis) throws EtatInvalide {
        if (colis.type.equals(TypeColis.NouveauColis)) {
            var reference = genererReference();
            return this.colisExistants.enregistrerColis(colis.toBuilder()
                    .reference(reference)
                    .type(ColisPrisEnCharge)
                    .build());
        } else {
            throw new EtatInvalide("Nouveau colis attendu");
        }
    }

    public Colis gererColis(Colis colis) {
        if (colis.type.equals(TypeColis.NouveauColis)) {
            throw new EtatInvalide("Le colis ne doit pas être un nouveau colis");
        } else {
            var colisExistant = this.colisExistants.chercherColisExistantParReference(colis.reference);
            if (Objects.isNull(colisExistant)) {
                throw new ColisNonTrouve(colis.reference);
            }

            if ((colisExistant.type.equals(ColisPrisEnCharge) && colis.type.equals(ColisEnCoursDAcheminement)) ||
                (colisExistant.type.equals(ColisEnCoursDAcheminement) && colis.type.equals(ColisEnCoursDAcheminement)) ||
                (colisExistant.type.equals(ColisEnCoursDAcheminement) && colis.type.equals(ColisRecu))) {

                if (colisExistant.type.equals(ColisPrisEnCharge) &&
                    colisExistant.dateDEnvoi.isBefore(LocalDateTime.now().minusMonths(1))) {
                    throw new EtatInvalide("La prise en charge date de plus d'1 mois");
                }

                return this.colisExistants.mettreAJourColis(colis);
            }
            if (colisExistant.type.equals(ColisPrisEnCharge)) {
                throw new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\"");
            }
            if (colisExistant.type.equals(ColisEnCoursDAcheminement)) {
                throw new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\" ou \"ColisPrisEnCharge\"");
            }
            if (colisExistant.type.equals(ColisRecu)) {
                throw new EtatInvalide("Le colis est déjà reçu");
            }
            throw new EtatInvalide("Cas non géré");
        }
    }



    private String genererReference() {
        return UUID.randomUUID().toString();
    }
}

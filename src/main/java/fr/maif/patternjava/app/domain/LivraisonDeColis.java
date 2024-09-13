package fr.maif.patternjava.app.domain;

import fr.maif.patternjava.app.domain.errors.ColisNonTrouve;
import fr.maif.patternjava.app.domain.errors.EtatInvalide;
import fr.maif.patternjava.app.domain.models.Colis;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component("LivraisonDeColisClassic")
public class LivraisonDeColis {

    public final ColisExistants colisExistants;

    public LivraisonDeColis(ColisExistants colisExistants) {
        this.colisExistants = colisExistants;
    }

    public List<Colis.ColisExistant> listerColis() {
        return this.colisExistants.listerColis();
    }

    public Colis prendreEnChargeLeColis(Colis colis) throws EtatInvalide {
        if (colis instanceof Colis.NouveauColis nouveauColis) {
            var reference = genererReference();
            return this.colisExistants.enregistrerColis(nouveauColis.toColisPrisEnCharge(reference));
        } else {
            throw new EtatInvalide("Nouveau colis attendu");
        }
    }


    public Colis gererColis(Colis colis) {
        return switch (colis) {
            case Colis.NouveauColis nouveauColis -> throw new EtatInvalide("On attend un colis existant");
            case Colis.ColisExistant colisAGerer -> {
                Colis.ColisExistant colisExistant = colisExistants.chercherColisExistantParReference(colisAGerer.reference());
                if (Objects.isNull(colisExistant)) {
                    throw new ColisNonTrouve(colisAGerer.reference());
                }
                yield gererColisExistant(colisExistant, colisAGerer);
            }
        };
    }

    Colis gererColisExistant(Colis.ColisExistant colisExistant, Colis.ColisExistant colisAGerer) {
        record ExistantEtAGerer(Colis.ColisExistant colisExistant, Colis.ColisExistant colisAGerer) {}

        return switch (new ExistantEtAGerer(colisExistant, colisAGerer)) {
            case ExistantEtAGerer(Colis.ColisPrisEnCharge         colisPrisEnCharge, Colis.ColisEnCoursDAcheminement __) when colisPrisEnCharge.dateDEnvoi().isBefore(LocalDateTime.now().minusMonths(1)) -> throw new EtatInvalide("La prise en charge date de plus d'1 mois");
            case ExistantEtAGerer(Colis.ColisPrisEnCharge         __, Colis.ColisEnCoursDAcheminement colisEnCoursAGerer) -> colisExistants.mettreAJourColis(colisEnCoursAGerer);
            case ExistantEtAGerer(Colis.ColisEnCoursDAcheminement __, Colis.ColisEnCoursDAcheminement colisEnCoursAGerer) -> colisExistants.mettreAJourColis(colisEnCoursAGerer);
            case ExistantEtAGerer(Colis.ColisEnCoursDAcheminement __, Colis.ColisRecu colisEnCoursAGerer) -> colisExistants.mettreAJourColis(colisEnCoursAGerer);
            case ExistantEtAGerer(Colis.ColisPrisEnCharge         __, var ___) -> throw new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\"");
            case ExistantEtAGerer(Colis.ColisEnCoursDAcheminement __, var ___) -> throw new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\" ou \"ColisPrisEnCharge\"");
            case ExistantEtAGerer(Colis.ColisRecu                 __, var ___) -> throw new EtatInvalide("Le colis est déja reçu");
        };
    }

    private String genererReference() {
        return UUID.randomUUID().toString();
    }
}

package fr.maif.patternjava.app.domain;

import fr.maif.patternjava.app.domain.models.ColisOuErreur;
import fr.maif.patternjava.app.domain.models.ColisOuErreur.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component("LivraisonDeColis")
public class LivraisonDeColis {

    public final ColisExistants colisExistants;

    public LivraisonDeColis(ColisExistants colisExistants) {
        this.colisExistants = colisExistants;
    }

    public List<ColisOuErreur.ColisExistant> listerColis() {
        return this.colisExistants.listerColis();
    }


    public ColisOuErreur prendreEnChargeLeColis(ColisOuErreur.Colis colis) {
        return switch (colis) {
            case NouveauColis nouveauColis -> this.colisExistants.enregistrerColis(nouveauColis.toColisPrisEnCharge(genererReference()));
            default -> new EtatInvalide("Nouveau colis attendu");
        };
    }

    public ColisOuErreur gererColis(Colis colis) {
        return switch (colis) {
            case NouveauColis nouveauColis -> new EtatInvalide("On attend un colis existant");
            case ColisExistant colisAGerer -> {
                ColisExistant colisExistant = colisExistants.chercherColisExistantParReference(colisAGerer.reference());
                if (Objects.isNull(colisExistant)) {
                    yield new ColisNonTrouve(colisAGerer.reference().value());
                }
                yield gererColisExistant(colisExistant, colisAGerer);
            }
        };
    }

    ColisOuErreur gererColisExistant(ColisExistant colisExistant, ColisExistant colisAGerer) {
        record ExistantEtAGerer(ColisExistant colisExistant, ColisExistant colisAGerer) {}

        return switch (new ExistantEtAGerer(colisExistant, colisAGerer)) {
            case ExistantEtAGerer(ColisPrisEnCharge         colisPrisEnCharge, ColisEnCoursDAcheminement __) when colisPrisEnCharge.dateDEnvoi().isBefore(LocalDateTime.now().minusMonths(1)) -> new EtatInvalide("La prise en charge date de plus d'1 mois");
            case ExistantEtAGerer(ColisPrisEnCharge         __, ColisEnCoursDAcheminement colisEnCoursAGerer) -> gererColisEnCoursDAcheminement(colisEnCoursAGerer);
            case ExistantEtAGerer(ColisEnCoursDAcheminement __, ColisEnCoursDAcheminement colisEnCoursAGerer) -> gererColisEnCoursDAcheminement(colisEnCoursAGerer);
            case ExistantEtAGerer(ColisEnCoursDAcheminement __, ColisRecu colisEnCoursAGerer) -> gererColisRecu(colisEnCoursAGerer);
            case ExistantEtAGerer(ColisPrisEnCharge         __, var ___) -> new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\"");
            case ExistantEtAGerer(ColisEnCoursDAcheminement __, var ___) -> new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\" ou \"ColisPrisEnCharge\"");
            case ExistantEtAGerer(ColisRecu                 __, var ___) -> new EtatInvalide("Le colis est déja reçu");
        };
    }


    private ColisExistant gererColisRecu(ColisRecu colisEnCoursAGerer) {
        return colisExistants.mettreAJourColis(colisEnCoursAGerer);
    }

    private ColisExistant gererColisEnCoursDAcheminement(ColisEnCoursDAcheminement colisEnCoursAGerer) {
        return colisExistants.mettreAJourColis(colisEnCoursAGerer);
    }

    private ReferenceColis genererReference() {
        return new ReferenceColis(UUID.randomUUID().toString());
    }
}
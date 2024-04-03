package fr.maif.patternjava.appv2.domain;

import fr.maif.patternjava.appv2.domain.errors.ColisNonTrouve;
import fr.maif.patternjava.appv2.domain.errors.EtatInvalide;
import fr.maif.patternjava.appv2.domain.models.Colis;
import fr.maif.patternjava.appv2.domain.models.Colis.ColisEnCoursDAcheminement;
import fr.maif.patternjava.appv2.domain.models.Colis.ColisExistant;
import fr.maif.patternjava.appv2.domain.models.Colis.ColisPrisEnCharge;
import fr.maif.patternjava.appv2.domain.models.Colis.ColisRecu;
import fr.maif.patternjava.appv2.domain.models.TypeColis;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static fr.maif.patternjava.appv1.domain.models.TypeColis.*;

@Component("LivraisonDeColis")
public class LivraisonDeColis {

    public final ColisExistants colisExistants;

    public LivraisonDeColis(ColisExistants colisExistants) {
        this.colisExistants = colisExistants;
    }

    public List<ColisExistant> listerColis() {
        return this.colisExistants.listerColis();
    }


    public Colis prendreEnChargeLeColis(Colis colis) throws EtatInvalide {
        return switch (colis) {
            case Colis.NouveauColis nouveauColis -> this.colisExistants.enregistrerColis(nouveauColis.toColisPrisEnCharge(genererReference()));
            default -> throw new EtatInvalide("Nouveau colis attendu");
        };
    }

    Colis gererColisExistant(ColisExistant colisExistant, ColisExistant colisAGerer) {
        record ExistantEtAGerer(ColisExistant colisExistant, ColisExistant colisAGerer) {}

        return switch (new ExistantEtAGerer(colisExistant, colisAGerer)) {
            case ExistantEtAGerer(ColisPrisEnCharge         ignored, ColisEnCoursDAcheminement colisEnCoursAGerer) -> gererColisEnCoursDAcheminement(colisEnCoursAGerer);
            case ExistantEtAGerer(ColisEnCoursDAcheminement ignored, ColisEnCoursDAcheminement colisEnCoursAGerer) -> gererColisEnCoursDAcheminement(colisEnCoursAGerer);
            case ExistantEtAGerer(ColisEnCoursDAcheminement ignored, ColisRecu colisEnCoursAGerer) -> gererColisRecu(colisEnCoursAGerer);
            case ExistantEtAGerer(ColisPrisEnCharge         ignored, var ignored2) -> throw new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\"");
            case ExistantEtAGerer(ColisEnCoursDAcheminement ignored, var ignored2) -> throw new EtatInvalide("On attend un colis à l'état \"ColisEnCoursDAcheminement\" ou \"ColisPrisEnCharge\"");
            case ExistantEtAGerer(ColisRecu                 ignored, var ignored2) -> throw new EtatInvalide("Le colis est déja reçu");
        };
    }


    private ColisExistant gererColisRecu(ColisRecu colisEnCoursAGerer) {
        return colisExistants.mettreAJourColis(colisEnCoursAGerer);
    }

    private ColisExistant gererColisEnCoursDAcheminement(ColisEnCoursDAcheminement colisEnCoursAGerer) {
        return colisExistants.mettreAJourColis(colisEnCoursAGerer);
    }

    public Colis gererColis(Colis colis) {
        return switch (colis) {
            case Colis.NouveauColis nouveauColis -> prendreEnChargeLeColis(nouveauColis);
            case ColisExistant colisAGerer -> {
                ColisExistant colisExistant = colisExistants.chercherColisExistantParReference(colisAGerer.reference());
                if (Objects.isNull(colisExistant)) {
                    throw new ColisNonTrouve(colisAGerer.reference());
                }
                yield  gererColisExistant(colisExistant, colisAGerer);
            }
        };
    }

    private String genererReference() {
        return UUID.randomUUID().toString();
    }
}

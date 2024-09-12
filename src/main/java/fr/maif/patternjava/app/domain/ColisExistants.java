package fr.maif.patternjava.app.domain;

import fr.maif.patternjava.app.domain.models.Colis;

import java.util.List;

public interface ColisExistants {

    Colis.ColisExistant chercherColisExistantParReference(String referenceColis);

    Colis.ColisExistant enregistrerColis(Colis.ColisExistant colisPrisEnCharge);

    Colis.ColisExistant mettreAJourColis(Colis.ColisExistant colisExistant);

    List<Colis.ColisExistant> listerColis();
}

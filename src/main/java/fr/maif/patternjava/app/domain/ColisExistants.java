package fr.maif.patternjava.app.domain;

import fr.maif.patternjava.app.domain.models.ColisOuErreur;

import java.util.List;

public interface ColisExistants {

    ColisOuErreur.ColisExistant chercherColisExistantParReference(String referenceColis);

    ColisOuErreur.ColisExistant enregistrerColis(ColisOuErreur.ColisExistant colisPrisEnCharge);

    ColisOuErreur.ColisExistant mettreAJourColis(ColisOuErreur.ColisExistant colisExistant);

    List<ColisOuErreur.ColisExistant> listerColis();
}

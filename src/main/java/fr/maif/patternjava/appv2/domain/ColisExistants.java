package fr.maif.patternjava.appv2.domain;

import fr.maif.patternjava.appv2.domain.models.ColisOuErreur.ColisExistant;

import java.util.List;

public interface ColisExistants {

    ColisExistant chercherColisExistantParReference(String referenceColis);

    ColisExistant enregistrerColis(ColisExistant colisPrisEnCharge);

    ColisExistant mettreAJourColis(ColisExistant colisExistant);

    List<ColisExistant> listerColis();
}

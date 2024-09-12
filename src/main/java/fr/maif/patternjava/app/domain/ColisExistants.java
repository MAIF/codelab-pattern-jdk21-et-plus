package fr.maif.patternjava.app.domain;

import fr.maif.patternjava.app.domain.models.Colis;

import java.util.List;

public interface ColisExistants {

    Colis chercherColisExistantParReference(String referenceColis);

    Colis enregistrerColis(Colis colisPrisEnCharge);

    Colis mettreAJourColis(Colis colisExistant);

    List<Colis> listerColis();
}

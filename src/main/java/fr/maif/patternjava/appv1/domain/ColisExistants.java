package fr.maif.patternjava.appv1.domain;

import fr.maif.patternjava.appv1.domain.models.Colis;

import java.util.List;

public interface ColisExistants {

    Colis chercherColisExistantParReference(String referenceColis);

    Colis enregistrerColis(Colis colisPrisEnCharge);

    Colis mettreAJourColis(Colis colisExistant);

    List<Colis> listerColis();
}

package fr.maif.patternjava.app.domain.impl;

import fr.maif.patternjava.app.domain.ColisExistants;
import fr.maif.patternjava.app.domain.models.ColisOuErreur;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ColisExistantInMemory implements ColisExistants {

    private final ConcurrentHashMap<ColisOuErreur.ReferenceColis, ColisOuErreur.ColisExistant> store = new ConcurrentHashMap<>();

    @Override
    public ColisOuErreur.ColisExistant chercherColisExistantParReference(ColisOuErreur.ReferenceColis referenceColis) {
        return store.get(referenceColis);
    }

    @Override
    public ColisOuErreur.ColisExistant enregistrerColis(ColisOuErreur.ColisExistant colisPrisEnCharge) {
        return store.compute(colisPrisEnCharge.reference(), (id, colisExistant) -> colisPrisEnCharge);
    }

    @Override
    public ColisOuErreur.ColisExistant mettreAJourColis(ColisOuErreur.ColisExistant colis) {
        return store.compute(colis.reference(), (id, colisExistant) -> colis);
    }

    @Override
    public List<ColisOuErreur.ColisExistant> listerColis() {
        return store.values().stream().toList();
    }
}
